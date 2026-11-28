package com.example.bookeasy.service;

import java.time.format.DateTimeParseException;

import com.example.bookeasy.controller.dto.CreateBookingRequest;
import com.example.bookeasy.entities.Booking;
import com.example.bookeasy.entities.Booking.BookingStatus;
import com.example.bookeasy.exception.RestAPIException;
import com.example.bookeasy.mapper.BookingMapper;
import com.example.bookeasy.repository.BookingRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookingService {

	private final BookingRepository bookingRepository;
	private final BookingMapper bookingMapper;

	public BookingService(BookingRepository bookingRepository) {
		this.bookingRepository = bookingRepository;
		this.bookingMapper = new BookingMapper();
	}

	@Transactional
	public com.example.bookeasy.controller.dto.Booking createBooking(CreateBookingRequest request) {
		try {
			Booking booking = bookingMapper.toEntity(request);
			if (booking.getStatus() == null) {
				booking.setStatus(BookingStatus.CONFIRMED);
			}

			Booking persistedBooking = bookingRepository.save(booking);
			return bookingMapper.toDto(persistedBooking);
		} catch (DateTimeParseException exception) {
			RestAPIException restException = new RestAPIException(
					HttpStatus.BAD_REQUEST,
					"INVALID_REQUEST",
					"Invalid booking date or time.");
			restException.initCause(exception);
			throw restException;
		} catch (DataAccessException exception) {
			RestAPIException restException = new RestAPIException(
					HttpStatus.INTERNAL_SERVER_ERROR,
					"SERVER_ERROR",
					"Unable to process booking request.");
			restException.initCause(exception);
			throw restException;
		}
	}
}
