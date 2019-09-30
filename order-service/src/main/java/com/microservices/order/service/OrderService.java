package com.microservices.order.service;

import com.microservices.order.dto.OrderDto;
import com.microservices.order.entity.OrderStatus;

import java.util.List;

public interface OrderService {

    List<OrderDto> getAllOrders();

    //OrderDto getOrderById(int orderId);

    //OrderDto addItem(int orderId, ItemAdditionalParametersDto parametersDto);

    //OrderDto setStatus(int orderId, OrderStatus status);
}
