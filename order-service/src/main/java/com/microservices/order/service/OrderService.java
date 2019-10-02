package com.microservices.order.service;

import com.microservices.order.dto.ItemChangeAmountDto;
import com.microservices.order.dto.OrderDto;
import com.microservices.order.entity.OrderStatus;
import org.springframework.stereotype.Service;

import java.util.List;

public interface OrderService {

    List<OrderDto> getAllOrders();

    OrderDto getOrderById(int orderId);

    OrderDto addItemToOrder(int orderId, ItemChangeAmountDto additionDto);

    OrderDto setOrderStatus(int orderId, OrderStatus status);
}
