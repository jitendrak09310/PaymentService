package com.jk.paymentservice.service;

public interface PaymentAuditService {

	void log(String txnId, String event, String status, String message);

}
