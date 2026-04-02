package com.jk.paymentservice.service.strategy;

import org.springframework.stereotype.Component;

import com.jk.paymentservice.dto.PaymentRequest;
import com.jk.paymentservice.dto.PaymentResponse;
import com.jk.paymentservice.enums.PaymentStatus;

import jakarta.validation.Valid;

@Component("stripeStrategy")
public class StripeStrategy implements PaymentStrategy {

	@Override
	public PaymentResponse processPayment(@Valid PaymentRequest request) {
		// hardcoded.
		return PaymentResponse.builder().status(PaymentStatus.SUCCESS)
				.message("Payment processed successfully via Stripe").amount(request.getAmount()).build();

	}

}
