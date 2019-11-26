package com.microservices.order.controller;

import com.microservices.order.dto.*;
import com.microservices.order.entity.OrderStatus;
import com.microservices.order.feign.ItemServiceFeignClient;
import com.microservices.order.feign.PaymentServiceFeignClient;
import com.microservices.order.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;

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
    private PaymentServiceFeignClient paymentServiceFeignClient;

    @Autowired
    private ItemServiceFeignClient itemServiceFeignClient;

    @GetMapping
    public List<OrderDto> getAllOrders(){
        return orderService.getAllOrders();
    }

    @GetMapping(value = "{orderId}")
    public OrderDto getOrderById(
        @PathVariable int orderId){

        return orderService.getOrderById(orderId);
    }

    @PostMapping(value = {"{orderId}/adding", "/adding"})
    public OrderDto addItemToOrder(
        @PathVariable(required = false) Optional<Integer> orderId,
        @RequestBody ItemChangeAmountDto additionDto) {

        int id = additionDto.getItemId();
        int amount = additionDto.getAmount();
        itemServiceFeignClient.decreaseById(id, amount);

        OrderDto resultOrder;
        if (orderId.isPresent()) {
            resultOrder = orderService.addItemToOrder(orderId.get(), additionDto);
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

        PaymentDto paymentDto = paymentServiceFeignClient.performPayment(new PaymentCreationDto(orderId, userDetailsDto.getCardAuthorizationInfo()));

        if (paymentDto.getStatus() == PaymentStatus.PERFORMED) {
            orderDto = orderService.setOrderStatus(orderId, OrderStatus.PAID);
        } else if (paymentDto.getStatus() == PaymentStatus.FAILED) {
            orderDto = orderService.setOrderStatus(orderId, OrderStatus.FAILED);
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
