package com.example.bookeasy;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "bookings", indexes = {
		@Index(name = "idx_bookings_booking_date", columnList = "booking_date"),
		@Index(name = "idx_bookings_customer_email", columnList = "customer_email")
})
public class Booking {

	private static final int CUSTOMER_NAME_MAX_LENGTH = 100;
	private static final int CUSTOMER_EMAIL_MAX_LENGTH = 254;
	private static final int STATUS_MAX_LENGTH = 20;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank
	@Size(max = CUSTOMER_NAME_MAX_LENGTH)
	@Column(name = "customer_name", nullable = false, length = CUSTOMER_NAME_MAX_LENGTH)
	private String customerName;

	@NotBlank
	@Email
	@Size(max = CUSTOMER_EMAIL_MAX_LENGTH)
	@Column(name = "customer_email", nullable = false, length = CUSTOMER_EMAIL_MAX_LENGTH)
	private String customerEmail;

	@NotNull
	@Column(name = "booking_date", nullable = false)
	private LocalDate bookingDate;

	@NotNull
	@Column(name = "booking_time", nullable = false)
	private LocalTime bookingTime;

	@Column(name = "description", columnDefinition = "TEXT")
	private String description;

	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false, length = STATUS_MAX_LENGTH)
	private BookingStatus status;

	@Column(name = "created_at", nullable = false, updatable = false)
	private LocalDateTime createdAt;

	@Column(name = "updated_at", nullable = false)
	private LocalDateTime updatedAt;

	protected Booking() {
	}

	public Booking(String customerName, String customerEmail, LocalDate bookingDate, LocalTime bookingTime,
			String description, BookingStatus status) {
		this.customerName = customerName;
		this.customerEmail = customerEmail;
		this.bookingDate = bookingDate;
		this.bookingTime = bookingTime;
		this.description = description;
		this.status = status;
	}

	@PrePersist
	void onCreate() {
		LocalDateTime now = LocalDateTime.now();
		createdAt = now;
		updatedAt = now;
	}

	@PreUpdate
	void onUpdate() {
		updatedAt = LocalDateTime.now();
	}

	public Long getId() {
		return id;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getCustomerEmail() {
		return customerEmail;
	}

	public void setCustomerEmail(String customerEmail) {
		this.customerEmail = customerEmail;
	}

	public LocalDate getBookingDate() {
		return bookingDate;
	}

	public void setBookingDate(LocalDate bookingDate) {
		this.bookingDate = bookingDate;
	}

	public LocalTime getBookingTime() {
		return bookingTime;
	}

	public void setBookingTime(LocalTime bookingTime) {
		this.bookingTime = bookingTime;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public BookingStatus getStatus() {
		return status;
	}

	public void setStatus(BookingStatus status) {
		this.status = status;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}
}
