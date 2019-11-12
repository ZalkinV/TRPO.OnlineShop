package com.microservices.order.controller;

import com.microservices.order.dto.*;
import com.microservices.order.entity.OrderStatus;
import com.microservices.order.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@EnableRabbit
@RestController
@RequestMapping("api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping
    public List<OrderDto> getAllOrders(){
        return orderService.getAllOrders();
    }

    @GetMapping(value = "{orderId}")
    public OrderDto getOrderById(
        @PathVariable int orderId){

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

        OrderDto orderDto = orderService.setOrderStatus(orderId, orderStatus);
        if (orderStatus == OrderStatus.CANCELED) {
            List<OrderItemDto> itemsToReturn = orderDto.getItems();
            returnItems(itemsToReturn);
            cancelPayment(orderId);
        }

        return orderDto;
    }

    @PutMapping(value = "{orderId}/payment")
    public OrderDto performPayment(
        @PathVariable int orderId,
        @RequestBody UserDetailsDto userDetailsDto){

        OrderDto orderDto = orderService.getOrderById(orderId);

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<PaymentCreationDto> paymentCreationDtoHttpEntity = new HttpEntity<>(
                new PaymentCreationDto(orderId, userDetailsDto.getCardAuthorizationInfo()), headers);

        ResponseEntity<PaymentDto> result = restTemplate.exchange("http://localhost:8082/payment",
                HttpMethod.POST, paymentCreationDtoHttpEntity, PaymentDto.class);

        if (result.getStatusCode() == HttpStatus.OK) {
            PaymentDto paymentDto = result.getBody();
            if (paymentDto.getStatus() == PaymentStatus.PERFORMED) {
                orderDto = orderService.setOrderStatus(orderId, OrderStatus.PAID);
            } else if (paymentDto.getStatus() == PaymentStatus.FAILED) {
                orderDto = orderService.setOrderStatus(orderId, OrderStatus.FAILED);
            }
        } else {
            throw new IllegalArgumentException("Something went wrong in payment service. HttpStatus code: " + result.getStatusCode());
        }

        return orderDto;
    }

    public void cancelPayment(int orderId) {
        rabbitTemplate.convertAndSend("directExchange", "payment", orderId);
        logger.info("Send message to payment with orderId=" + orderId);
    }

    public void returnItems(List<OrderItemDto> items) {
        rabbitTemplate.convertAndSend("directExchange", "item", items);
        logger.info("Send message to item with items count=" + items.size());
    }
}
