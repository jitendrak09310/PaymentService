package com.jk.paymentservice.service;

import com.jk.paymentservice.dto.PaymentRequest;
import com.jk.paymentservice.dto.PaymentResponse;

import jakarta.validation.Valid;

public interface PaymentsService {

	PaymentResponse processPayment(@Valid PaymentRequest request, String idempotencyKey);

	PaymentResponse getTransactionById(@Valid String transactionId);

}
