package com.jk.paymentservice.service.Impl;

import java.util.Map;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jk.paymentservice.dto.PaymentRequest;
import com.jk.paymentservice.dto.PaymentResponse;
import com.jk.paymentservice.entity.Payment;
import com.jk.paymentservice.enums.PaymentStatus;
import com.jk.paymentservice.exception.PaymentException;
import com.jk.paymentservice.repository.PaymentRepository;
import com.jk.paymentservice.service.PaymentAuditService;
import com.jk.paymentservice.service.PaymentsService;
import com.jk.paymentservice.service.strategy.PaymentStrategy;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentsServiceImpl implements PaymentsService {

	private final PaymentRepository paymentRepository;

	private final PaymentAuditService auditService;

	private final Map<String, PaymentStrategy> strategies;

	@Override
	@Transactional
	public PaymentResponse processPayment(@Valid PaymentRequest request, String idempotencyKey) {

		Optional<Payment> existing = paymentRepository.findByIdempotencyKey(idempotencyKey);

		log.info("idempotency key is : " + idempotencyKey + "   " + existing.isPresent());
		Payment payment;
		if (existing.isPresent()) {
			payment = existing.get();

			switch (payment.getStatus()) {
			case SUCCESS: {
				return PaymentResponse.builder().amount(payment.getAmount()).status(payment.getStatus())
						.transactionId(payment.getTransactionId())
						.message("Duplicate Request - Returning the existing result.").build();
			}
			case PENDING: {
				throw new PaymentException("Payment is already in progress !");
			}
			case FAILED: {
				// retrying...
				log.info("Retrying failed payment for key : " + idempotencyKey);
				payment.setStatus(PaymentStatus.PENDING);
				paymentRepository.save(payment);
				auditService.log(payment.getTransactionId(), "PAYMENT-RETRY", "PENDING", "Retrying Failed Payment");
				break;
			}
			default:
				throw new IllegalArgumentException("Unexpected value: " + payment.getStatus());
			}

		} else {
			String transactionId = UUID.randomUUID().toString();

			payment = Payment.builder().transactionId(transactionId).amount(request.getAmount())
					.status(PaymentStatus.PENDING).paymentMethod(request.getPaymentMethod())
					.idempotencyKey(idempotencyKey).build();

			paymentRepository.save(payment);

			// audit init..
			auditService.log(transactionId, "PAYMENT-INIT", "PENDING", "Payment-started");
		}

		try {

			PaymentStrategy strategy = strategies.get(request.getPaymentMethod().name().toLowerCase() + "Strategy");
			if (strategy == null) {
				throw new PaymentException("Unsupported Payment method.");
			}

			// audit gateway call..
			auditService.log(payment.getTransactionId(), "GATEWAY-CALL", "PENDING", "Calling payment Gateway..");

			PaymentResponse response = strategy.processPayment(request);
			payment.setStatus(PaymentStatus.SUCCESS);
			paymentRepository.save(payment);

			// audit Success.
			auditService.log(payment.getTransactionId(), "PAYMENT-SUCCESS", "SUCCESS", response.getMessage());

			return PaymentResponse.builder().transactionId(payment.getTransactionId()).status(response.getStatus())
					.amount(response.getAmount()).message(response.getMessage()).build();

		} catch (Exception e) {
			payment.setStatus(PaymentStatus.FAILED);
			paymentRepository.save(payment);
			// audit Failure.
			auditService.log(payment.getTransactionId(), "PAYMENT-FAILED", "FAILED", e.getMessage());
			throw new PaymentException("Payment Failed .. " + e.getMessage());
		}

	}

	@Override
	public PaymentResponse getTransactionById(@Valid String transactionId) {
		Payment payment = paymentRepository.findByTransactionId(transactionId)
				.orElseThrow(() -> new PaymentException("Transaction not found"));

		return PaymentResponse.builder().transactionId(payment.getTransactionId()).amount(payment.getAmount())
				.message("Transaction Fetced Successfully").status(payment.getStatus()).build();
	}

}
