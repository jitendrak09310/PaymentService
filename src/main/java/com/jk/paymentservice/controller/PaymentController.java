package com.jk.paymentservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jk.paymentservice.dto.PaymentRequest;
import com.jk.paymentservice.dto.PaymentResponse;
import com.jk.paymentservice.service.PaymentsService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class PaymentController {

	private final PaymentsService paymentService;

	@PostMapping
	public ResponseEntity<PaymentResponse> processPayment(@Valid @RequestBody PaymentRequest request,
			@RequestHeader("Idempotency-key") String idempotencyKey) {
		return ResponseEntity.status(201).body(paymentService.processPayment(request, idempotencyKey));
	}

	@GetMapping("/gettransaction/{transactionId}")
	public ResponseEntity<PaymentResponse> getTransaction(@PathVariable String transactionId) {
		return ResponseEntity.ok(paymentService.getTransactionById(transactionId));
	}
}
