package com.example.bookeasy.repository;

import java.time.LocalDate;
import java.util.List;

import com.example.bookeasy.entities.Booking;
import com.example.bookeasy.entities.Booking.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking, Long> {

	List<Booking> findByCustomerEmail(String customerEmail);

	List<Booking> findByBookingDate(LocalDate bookingDate);

	List<Booking> findByStatus(BookingStatus status);

	List<Booking> findByBookingDateAndStatus(LocalDate bookingDate, BookingStatus status);
}
