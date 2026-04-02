package com.jk.paymentservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

	// 🔴 Custom Payment Exception
	@ExceptionHandler(PaymentException.class)
	public ResponseEntity<ErrorResponse> handlePaymentException(PaymentException ex) {

		ErrorResponse error = ErrorResponse.builder().message(ex.getMessage()).errorCode("PAYMENT_ERROR")
				.status(HttpStatus.BAD_REQUEST.value()).timestamp(LocalDateTime.now()).build();

		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}

	// 🔴 Validation Errors (@Valid)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {

		String message = ex.getBindingResult().getFieldErrors().stream().findFirst()
				.map(err -> err.getField() + " : " + err.getDefaultMessage()).orElse("Validation error");

		ErrorResponse error = ErrorResponse.builder().message(message).errorCode("VALIDATION_ERROR")
				.status(HttpStatus.BAD_REQUEST.value()).timestamp(LocalDateTime.now()).build();

		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}

	// 🔴 Illegal Argument
	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException ex) {

		ErrorResponse error = ErrorResponse.builder().message(ex.getMessage()).errorCode("INVALID_INPUT")
				.status(HttpStatus.BAD_REQUEST.value()).timestamp(LocalDateTime.now()).build();

		return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
	}

	// 🔴 Catch All (IMPORTANT)
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {

		ErrorResponse error = ErrorResponse.builder().message("Something went wrong").errorCode("INTERNAL_SERVER_ERROR")
				.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).timestamp(LocalDateTime.now()).build();

		return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}