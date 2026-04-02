package com.jk.paymentservice.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jk.paymentservice.entity.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

	Optional<Payment> findByTransactionId(String transactionId);

	Optional<Payment> findByIdempotencyKey(String idempotencyKey);

}
