package com.microservices.payment.service;

import com.microservices.payment.dto.PaymentCreationDto;
import com.microservices.payment.dto.PaymentDto;

public interface PaymentService {

    PaymentDto createPayment(PaymentCreationDto payment);

    PaymentDto getPaymentById(int paymentId);

    PaymentDto getPaymentByOrderId(int orderId);

    PaymentDto performPayment(int paymentId);

    PaymentDto cancelPayment(int paymentId);
}
