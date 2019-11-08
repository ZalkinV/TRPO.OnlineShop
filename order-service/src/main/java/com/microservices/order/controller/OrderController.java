package com.microservices.order.controller;

import com.microservices.order.dto.ItemChangeAmountDto;
import com.microservices.order.dto.OrderDto;
import com.microservices.order.dto.UserDetailsDto;
import com.microservices.order.entity.OrderStatus;
import com.microservices.order.service.OrderService;
import com.microservices.order.service.OrderServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListener;


import java.util.List;
import java.util.Optional;

@EnableRabbit
@RestController
@RequestMapping("api/orders")
public class OrderController {

    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private OrderService orderService;

    @GetMapping
    public List<OrderDto> getAllOrders(){
        return orderService.getAllOrders();
    }

    @GetMapping(value = "{orderId}")
    public OrderDto getOrderById(
        @PathVariable int orderId){

        testSend(orderId);
        return orderService.getOrderById(orderId);
    }

    @PostMapping(value = "{orderId}/adding")
    public OrderDto addItemToOrder(
        @PathVariable(required = false) Optional<String> orderIdString,
        @RequestBody ItemChangeAmountDto additionDto) {

        OrderDto resultOrder;
        if (orderIdString.isPresent()) {
            int orderId = Integer.parseInt(orderIdString.get());
            resultOrder = orderService.addItemToOrder(orderId, additionDto);
        } else {
            resultOrder = orderService.createNewOrder(additionDto);
        }
        return resultOrder;
    }

    @PutMapping(value = "{orderId}/status/{orderStatus}")
    public OrderDto setOrderStatus(
        @PathVariable int orderId,
        @PathVariable OrderStatus orderStatus) {

        return orderService.setOrderStatus(orderId, orderStatus);
    }

    @PutMapping(value = "{orderId}/payment")
    public OrderDto performPayment(
        @PathVariable int orderId,
        @RequestBody UserDetailsDto userDetailsDto){

        orderService.getOrderById(orderId);
        throw new IllegalArgumentException("I need help of payment-service to perform payment, but I cannot communicate with it :(");
    }

    @RabbitListener(queues = "qorder")
    public void handleMessage(String message) {
       logger.info(message);
    }

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void testSend(int orderId) {
        rabbitTemplate.convertAndSend("exchange", "order", "I am able to send message with orderId =" + orderId);
    }
}
