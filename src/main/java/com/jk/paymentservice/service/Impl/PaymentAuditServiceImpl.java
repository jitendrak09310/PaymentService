package com.jk.paymentservice.service.Impl;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.jk.paymentservice.entity.PaymentAudit;
import com.jk.paymentservice.repository.PaymentAuditRepository;
import com.jk.paymentservice.service.PaymentAuditService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentAuditServiceImpl implements PaymentAuditService {

	private final PaymentAuditRepository auditRepository;

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void log(String txnId, String event, String status, String message) {

		PaymentAudit audit = PaymentAudit.builder().transactionId(txnId).event(event).status(status).message(message)
				.timestamp(LocalDateTime.now()).build();
		auditRepository.save(audit);

	}
}
