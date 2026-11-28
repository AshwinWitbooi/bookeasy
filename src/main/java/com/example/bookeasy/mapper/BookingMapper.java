package com.example.bookeasy.mapper;

import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Component;

import com.example.bookeasy.controller.dto.CreateBookingRequest;
import com.example.bookeasy.entities.Booking;
import com.example.bookeasy.entities.Booking.BookingStatus;

@Component
public class BookingMapper {

	public Booking toEntity(CreateBookingRequest source) {
		if (source == null) {
			return null;
		}

		BookingStatus status;
		if (source.getStatus() == CreateBookingRequest.StatusEnum.CANCELLED) {
			status = BookingStatus.CANCELLED;
		} else {
			status = BookingStatus.CONFIRMED;
		}

		Booking target = new Booking(
				source.getCustomerName(),
				source.getCustomerEmail(),
				source.getBookingDate(),
				source.getBookingTime() == null ? null : LocalTime.parse(source.getBookingTime()),
				source.getDescription(),
				status);
		target.setVenue(source.getVenue());
		return target;
	}

	public com.example.bookeasy.controller.dto.Booking toDto(Booking source) {
		if (source == null) {
			return null;
		}

		com.example.bookeasy.controller.dto.Booking.StatusEnum status = null;
		if (source.getStatus() == BookingStatus.CONFIRMED) {
			status = com.example.bookeasy.controller.dto.Booking.StatusEnum.CONFIRMED;
		} else if (source.getStatus() == BookingStatus.CANCELLED) {
			status = com.example.bookeasy.controller.dto.Booking.StatusEnum.CANCELLED;
		}

		com.example.bookeasy.controller.dto.Booking target =
				new com.example.bookeasy.controller.dto.Booking();
		target.setId(source.getId() == null ? null : source.getId().toString());
		target.setCustomerName(source.getCustomerName());
		target.setCustomerEmail(source.getCustomerEmail());
		target.setBookingDate(source.getBookingDate());
		target.setBookingTime(source.getBookingTime() == null
				? null
				: source.getBookingTime().format(DateTimeFormatter.ofPattern("HH:mm")));
		target.setVenue(source.getVenue());
		target.setDescription(source.getDescription());
		target.setStatus(status);
		target.setCreatedAt(source.getCreatedAt() == null
				? null
				: OffsetDateTime.of(source.getCreatedAt(), ZoneOffset.UTC));
		target.setUpdatedAt(source.getUpdatedAt() == null
				? null
				: OffsetDateTime.of(source.getUpdatedAt(), ZoneOffset.UTC));
		return target;
	}
}
