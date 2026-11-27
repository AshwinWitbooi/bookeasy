package com.example.bookeasy.exception;

import java.time.OffsetDateTime;

import com.example.bookeasy.controller.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestGlobalExceptionHandler {

	@ExceptionHandler(RestAPIException.class)
	public ResponseEntity<ErrorResponse> restAPIExceptionHandler(RestAPIException exception) {
		ErrorResponse response = new ErrorResponse(
				exception.getError(),
				exception.getMessage(),
				OffsetDateTime.now());
		return new ResponseEntity<>(response, exception.getStatus());
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> exceptionHandler(Exception exception) {
		ErrorResponse response = new ErrorResponse(
				"Server Error",
				"Unable to process request.Please contact administrator",
				OffsetDateTime.now());
		return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
