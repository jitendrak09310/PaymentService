package com.jk.paymentservice.dto;

import java.math.BigDecimal;

import com.jk.paymentservice.enums.PaymentStatus;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaymentResponse {

	private String transactionId;
	private String message;
	private PaymentStatus status;
	private BigDecimal amount;

}
