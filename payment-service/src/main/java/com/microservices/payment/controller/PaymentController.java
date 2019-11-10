package com.microservices.payment.controller;

import com.microservices.payment.dto.PaymentCreationDto;
import com.microservices.payment.dto.PaymentDto;
import com.microservices.payment.service.PaymentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

import javax.validation.Valid;

@EnableRabbit
@RestController
@RequestMapping("payment")
public class PaymentController {
    private PaymentService paymentService;

    private static final Logger logger = LoggerFactory.getLogger(PaymentController.class);

    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    public PaymentDto performPayment(@Valid @RequestBody PaymentCreationDto paymentCreationDto) {
        return paymentService.performPayment(paymentCreationDto);
    }

    @GetMapping(value = "{payment_id}")
    public PaymentDto getPayment(@PathVariable int payment_id) {
        return paymentService.getPaymentById(payment_id);
    }

    @GetMapping(value = "order/{order_id}")
    public PaymentDto getPaymentByOrderId(@PathVariable int order_id) {
        return paymentService.getPaymentByOrderId(order_id);
    }

    @PutMapping(value = "{payment_id}/cancel")
    public PaymentDto cancelPayment(@PathVariable int payment_id) {
        return paymentService.cancelPayment(payment_id);
    }


    @RabbitListener(queues = "qpayment")
    public void cancelPayment(Message message) {
        logger.info("*********************" + message.toString());
    }
}
