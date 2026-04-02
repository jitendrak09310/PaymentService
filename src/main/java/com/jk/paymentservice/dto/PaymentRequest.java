package com.jk.paymentservice.dto;

import java.math.BigDecimal;

import com.jk.paymentservice.enums.PaymentMethod;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class PaymentRequest {

	@NotNull
	@Positive
	private BigDecimal amount;

	@NotNull
	private PaymentMethod paymentMethod;

}
