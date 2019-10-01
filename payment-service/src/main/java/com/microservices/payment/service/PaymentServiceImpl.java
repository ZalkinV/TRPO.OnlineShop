package com.microservices.payment.service;

import com.microservices.payment.dto.PaymentDto;
import com.microservices.payment.entity.PaymentEntity;
import com.microservices.payment.entity.PaymentStatus;
import com.microservices.payment.repository.PaymentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl implements PaymentService {
    private final Logger logger = LoggerFactory.getLogger(PaymentServiceImpl.class);

    private PaymentRepository paymentRepository;

    @Autowired
    public PaymentServiceImpl(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Override
    public PaymentDto getPaymentById(int paymentId) {
        PaymentEntity payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new IllegalArgumentException("No payment with id " + paymentId));

        return payment.toPaymentDto();
    }

    @Override
    public PaymentDto getPaymentByOrderId(int orderId) {
        PaymentEntity payment = paymentRepository.findByOrderId(orderId)
                .orElseThrow(() -> new IllegalArgumentException("No payment with order_id " + orderId));

        return payment.toPaymentDto();
    }

    @Override
    public PaymentDto performPayment(int paymentId) {
        PaymentEntity payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new IllegalArgumentException("No payment with id " + paymentId));

        payment.setStatus(PaymentStatus.PERFORMED);

        logger.info("Payment with id: {} performed", paymentId);
        return payment.toPaymentDto();
    }

    @Override
    public PaymentDto cancelPayment(int paymentId) {
        PaymentEntity payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new IllegalArgumentException("No payment with id " + paymentId));

        payment.setStatus(PaymentStatus.CANCELED);

        logger.info("Payment with id: {} canceled", paymentId);
        return payment.toPaymentDto();
    }
}
