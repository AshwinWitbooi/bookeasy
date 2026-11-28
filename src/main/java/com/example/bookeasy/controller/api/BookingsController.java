package com.example.bookeasy.controller.api;

import com.example.bookeasy.controller.dto.Booking;
import com.example.bookeasy.controller.dto.CreateBookingRequest;
import com.example.bookeasy.service.BookingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BookingsController implements BookingsApi {

	private final BookingService bookingService;

	public BookingsController(BookingService bookingService) {
		this.bookingService = bookingService;
	}

	@Override
	public ResponseEntity<Booking> createBooking(
			String xCorrelationID,
			CreateBookingRequest createBookingRequest) {
		Booking booking = bookingService.createBooking(createBookingRequest);
		return ResponseEntity.status(HttpStatus.CREATED)
				.header("X-Correlation-ID", xCorrelationID)
				.body(booking);
	}
}
