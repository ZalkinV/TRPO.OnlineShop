package com.microservices.payment.service;

import com.microservices.payment.dto.PaymentCreationDto;
import com.microservices.payment.dto.PaymentDto;

public interface PaymentService {

    PaymentDto performPayment(PaymentCreationDto payment);

    PaymentDto getPaymentById(int paymentId);

    PaymentDto getPaymentByOrderId(int orderId);

    PaymentDto cancelPayment(int paymentId);
}
