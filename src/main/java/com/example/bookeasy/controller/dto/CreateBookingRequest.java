package com.example.bookeasy.controller.dto;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * CreateBookingRequest
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-11-26T20:43:48.292184838+02:00[Africa/Johannesburg]", comments = "Generator version: 7.12.0")
public class CreateBookingRequest {

  private String customerName;

  private String customerEmail;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
  private LocalDate bookingDate;

  private String bookingTime;

  private String venue;

  private @Nullable String description;

  /**
   * Booking status.
   */
  public enum StatusEnum {
    PENDING("pending"),
    
    CONFIRMED("confirmed"),
    
    CANCELLED("cancelled");

    private String value;

    StatusEnum(String value) {
      this.value = value;
    }

    @JsonValue
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static StatusEnum fromValue(String value) {
      for (StatusEnum b : StatusEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private StatusEnum status = StatusEnum.PENDING;

  public CreateBookingRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public CreateBookingRequest(String customerName, String customerEmail, LocalDate bookingDate, String bookingTime) {
    this.customerName = customerName;
    this.customerEmail = customerEmail;
    this.bookingDate = bookingDate;
    this.bookingTime = bookingTime;
  }

  public CreateBookingRequest customerName(String customerName) {
    this.customerName = customerName;
    return this;
  }

  /**
   * Customer's full name.
   * @return customerName
   */
  @NotNull @Size(min = 2, max = 150) 
  @Schema(name = "customerName", description = "Customer's full name.", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("customerName")
  public String getCustomerName() {
    return customerName;
  }

  public void setCustomerName(String customerName) {
    this.customerName = customerName;
  }

  public CreateBookingRequest customerEmail(String customerEmail) {
    this.customerEmail = customerEmail;
    return this;
  }

  /**
   * Customer's email address.
   * @return customerEmail
   */
  @NotNull @Size(max = 254) @jakarta.validation.constraints.Email 
  @Schema(name = "customerEmail", description = "Customer's email address.", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("customerEmail")
  public String getCustomerEmail() {
    return customerEmail;
  }

  public void setCustomerEmail(String customerEmail) {
    this.customerEmail = customerEmail;
  }

  public CreateBookingRequest bookingDate(LocalDate bookingDate) {
    this.bookingDate = bookingDate;
    return this;
  }

  /**
   * Booking date (YYYY-MM-DD).
   * @return bookingDate
   */
  @NotNull @Valid 
  @Schema(name = "bookingDate", description = "Booking date (YYYY-MM-DD).", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("bookingDate")
  public LocalDate getBookingDate() {
    return bookingDate;
  }

  public void setBookingDate(LocalDate bookingDate) {
    this.bookingDate = bookingDate;
  }

  public CreateBookingRequest bookingTime(String bookingTime) {
    this.bookingTime = bookingTime;
    return this;
  }

  /**
   * Booking time in 24-hour format (HH:mm).
   * @return bookingTime
   */
  @NotNull @Pattern(regexp = "^(?:[01]\\d|2[0-3]):[0-5]\\d$") 
  @Schema(name = "bookingTime", description = "Booking time in 24-hour format (HH:mm).", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("bookingTime")
  public String getBookingTime() {
    return bookingTime;
  }

  public void setBookingTime(String bookingTime) {
    this.bookingTime = bookingTime;
  }

  public CreateBookingRequest venue(String venue) {
    this.venue = venue;
    return this;
  }

  /**
   * Get venue
   * @return venue
   */
  @NotNull @Size(min = 2, max = 255) 
  @Schema(name = "venue", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("venue")
  public String getVenue() {
    return venue;
  }

  public void setVenue(String venue) {
    this.venue = venue;
  }

  public CreateBookingRequest description(String description) {
    this.description = description;
    return this;
  }

  /**
   * Additional booking information.
   * @return description
   */
  @Size(max = 1000) 
  @Schema(name = "description", description = "Additional booking information.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("description")
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public CreateBookingRequest status(StatusEnum status) {
    this.status = status;
    return this;
  }

  /**
   * Booking status.
   * @return status
   */
  
  @Schema(name = "status", description = "Booking status.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("status")
  public StatusEnum getStatus() {
    return status;
  }

  public void setStatus(StatusEnum status) {
    this.status = status;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CreateBookingRequest createBookingRequest = (CreateBookingRequest) o;
    return Objects.equals(this.customerName, createBookingRequest.customerName) &&
        Objects.equals(this.customerEmail, createBookingRequest.customerEmail) &&
        Objects.equals(this.bookingDate, createBookingRequest.bookingDate) &&
        Objects.equals(this.bookingTime, createBookingRequest.bookingTime) &&
        Objects.equals(this.venue, createBookingRequest.venue) &&
        Objects.equals(this.description, createBookingRequest.description) &&
        Objects.equals(this.status, createBookingRequest.status);
  }

  @Override
  public int hashCode() {
    return Objects.hash(customerName, customerEmail, bookingDate, bookingTime, venue, description, status);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CreateBookingRequest {\n");
    sb.append("    customerName: ").append(toIndentedString(customerName)).append("\n");
    sb.append("    customerEmail: ").append(toIndentedString(customerEmail)).append("\n");
    sb.append("    bookingDate: ").append(toIndentedString(bookingDate)).append("\n");
    sb.append("    bookingTime: ").append(toIndentedString(bookingTime)).append("\n");
    sb.append("    venue: ").append(toIndentedString(venue)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

