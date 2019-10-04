package com.microservices.payment.controller;

import com.microservices.payment.dto.PaymentCreationDto;
import com.microservices.payment.dto.PaymentDto;
import com.microservices.payment.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("payment")
public class PaymentController {
    private PaymentService paymentService;

    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    public PaymentDto createPayment(@Valid @RequestBody PaymentCreationDto paymentCreationDto) {
        return paymentService.createPayment(paymentCreationDto);
    }

    @GetMapping(value = "{payment_id}")
    public PaymentDto getPayment(@PathVariable int payment_id) {
        return paymentService.getPaymentById(payment_id);
    }

    @GetMapping(value = "order/{order_id}")
    public PaymentDto getPaymentByOrderId(@PathVariable int order_id) {
        return paymentService.getPaymentByOrderId(order_id);
    }

    @PutMapping(value = "{payment_id}/perform")
    public PaymentDto performPayment(@PathVariable int payment_id) {
        return paymentService.performPayment(payment_id);
    }

    @PutMapping(value = "{payment_id}/cancel")
    public PaymentDto cancelPayment(@PathVariable int payment_id) {
        return paymentService.cancelPayment(payment_id);
    }
}
