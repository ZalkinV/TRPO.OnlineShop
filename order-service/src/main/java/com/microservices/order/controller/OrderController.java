package com.microservices.order.controller;

import com.microservices.order.dto.OrderDto;
import com.microservices.order.entity.OrderStatus;
import com.microservices.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("order")
public class OrderController {
    private OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService){
        this.orderService = orderService;
    }

    @GetMapping
    public List<OrderDto> getAllOrders(){
        return orderService.getAllOrders();
    }

    @GetMapping(value = "{orderId}")
    public OrderDto getOrderById(@PathVariable int orderId){
        return orderService.getOrderById(orderId);
    }

    @PutMapping(value = "{orderId}/status/{orderStatus}")
    public OrderDto setOrderStatus(@PathVariable int orderId, @PathVariable OrderStatus orderStatus) {
        return orderService.setOrderStatus(orderId, orderStatus);
    }

}
