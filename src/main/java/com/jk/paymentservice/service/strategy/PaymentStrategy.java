package com.jk.paymentservice.service.strategy;

import com.jk.paymentservice.dto.PaymentRequest;
import com.jk.paymentservice.dto.PaymentResponse;

import jakarta.validation.Valid;

public interface PaymentStrategy {
	PaymentResponse processPayment(@Valid PaymentRequest request);
}
