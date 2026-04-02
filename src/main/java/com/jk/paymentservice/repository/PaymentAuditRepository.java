package com.jk.paymentservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jk.paymentservice.entity.PaymentAudit;

public interface PaymentAuditRepository extends JpaRepository<PaymentAudit, Long> {

}
