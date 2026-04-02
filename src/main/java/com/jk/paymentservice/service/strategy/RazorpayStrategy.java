package com.jk.paymentservice.service.strategy;

import org.springframework.stereotype.Component;

import com.jk.paymentservice.dto.PaymentRequest;
import com.jk.paymentservice.dto.PaymentResponse;
import com.jk.paymentservice.entity.Payment;
import com.jk.paymentservice.enums.PaymentStatus;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import jakarta.validation.Valid;

@Component("razorpayStrategy")
public class RazorpayStrategy implements PaymentStrategy {

	@Override
	@Retry(name = "paymentRetry", fallbackMethod = "fallback")
	@CircuitBreaker(name = "paymentCB", fallbackMethod = "fallback")
	public PaymentResponse processPayment(@Valid PaymentRequest request) {

		// simulate call

		if (Math.random() < 0.5) {
			throw new RuntimeException("Gateway Failed !");
		}

		// hardcoded.
		return PaymentResponse.builder().status(PaymentStatus.SUCCESS)
				.message("Payment processed successfully via RazorPay").amount(request.getAmount()).build();
	}

	public PaymentResponse fallback(PaymentRequest request, Throwable t) {

		return PaymentResponse.builder().status(PaymentStatus.FAILED).amount(request.getAmount())
				.message("Fallback triggered" + t.getMessage()).build();

	}

}
