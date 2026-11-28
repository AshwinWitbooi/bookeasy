package com.example.bookeasy.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

import com.example.bookeasy.controller.dto.CreateBookingRequest;
import com.example.bookeasy.entities.Booking;
import com.example.bookeasy.entities.Booking.BookingStatus;
import com.example.bookeasy.exception.RestAPIException;
import com.example.bookeasy.repository.BookingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;

class BookingServiceTest {

	private BookingRepository bookingRepository;
	private BookingService bookingService;

	@BeforeEach
	void setUp() {
		bookingRepository = org.mockito.Mockito.mock(BookingRepository.class);
		bookingService = new BookingService(bookingRepository);
	}

	@Test
	void createBookingPersistsMappedBookingAndReturnsResponse() {
		when(bookingRepository.save(any(Booking.class)))
				.thenAnswer(invocation -> invocation.getArgument(0));
		CreateBookingRequest request = validRequest();

		com.example.bookeasy.controller.dto.Booking response = bookingService.createBooking(request);

		ArgumentCaptor<Booking> bookingCaptor = ArgumentCaptor.forClass(Booking.class);
		verify(bookingRepository).save(bookingCaptor.capture());
		Booking savedBooking = bookingCaptor.getValue();
		assertEquals("Jane Doe", savedBooking.getCustomerName());
		assertEquals("jane.doe@example.com", savedBooking.getCustomerEmail());
		assertEquals(LocalDate.of(2026, 12, 1), savedBooking.getBookingDate());
		assertEquals(LocalTime.of(14, 30), savedBooking.getBookingTime());
		assertEquals("Main Hall", savedBooking.getVenue());
		assertEquals("Planning session", savedBooking.getDescription());
		assertEquals(BookingStatus.CONFIRMED, savedBooking.getStatus());

		assertEquals("Jane Doe", response.getCustomerName());
		assertEquals("jane.doe@example.com", response.getCustomerEmail());
		assertEquals(LocalDate.of(2026, 12, 1), response.getBookingDate());
		assertEquals("14:30", response.getBookingTime());
		assertEquals("Main Hall", response.getVenue());
		assertEquals("Planning session", response.getDescription());
		assertEquals(com.example.bookeasy.controller.dto.Booking.StatusEnum.CONFIRMED, response.getStatus());
	}

	@Test
	void createBookingPreservesCancelledStatus() {
		when(bookingRepository.save(any(Booking.class)))
				.thenAnswer(invocation -> invocation.getArgument(0));
		CreateBookingRequest request = validRequest();
		request.setStatus(CreateBookingRequest.StatusEnum.CANCELLED);

		com.example.bookeasy.controller.dto.Booking response = bookingService.createBooking(request);

		ArgumentCaptor<Booking> bookingCaptor = ArgumentCaptor.forClass(Booking.class);
		verify(bookingRepository).save(bookingCaptor.capture());
		assertEquals(BookingStatus.CANCELLED, bookingCaptor.getValue().getStatus());
		assertEquals(com.example.bookeasy.controller.dto.Booking.StatusEnum.CANCELLED, response.getStatus());
	}

	@Test
	void createBookingTranslatesInvalidTimeAndDoesNotSave() {
		CreateBookingRequest request = validRequest();
		request.setBookingTime("not-a-time");

		RestAPIException exception = assertThrows(
				RestAPIException.class,
				() -> bookingService.createBooking(request));

		assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
		assertEquals("INVALID_REQUEST", exception.getError());
		assertEquals("Invalid booking date or time.", exception.getMessage());
		assertNotNull(exception.getCause());
		org.junit.jupiter.api.Assertions.assertInstanceOf(DateTimeParseException.class, exception.getCause());
		verify(bookingRepository, never()).save(any(Booking.class));
	}

	@Test
	void createBookingTranslatesRepositoryFailure() {
		DataIntegrityViolationException repositoryFailure =
				new DataIntegrityViolationException("database failure");
		when(bookingRepository.save(any(Booking.class))).thenThrow(repositoryFailure);

		RestAPIException exception = assertThrows(
				RestAPIException.class,
				() -> bookingService.createBooking(validRequest()));

		assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatus());
		assertEquals("SERVER_ERROR", exception.getError());
		assertEquals("Unable to process booking request.", exception.getMessage());
		assertEquals(repositoryFailure, exception.getCause());
		verify(bookingRepository).save(any(Booking.class));
	}

	private static CreateBookingRequest validRequest() {
		CreateBookingRequest request = new CreateBookingRequest(
				"Jane Doe",
				"jane.doe@example.com",
				LocalDate.of(2026, 12, 1),
				"14:30");
		request.setVenue("Main Hall");
		request.setDescription("Planning session");
		return request;
	}
}
