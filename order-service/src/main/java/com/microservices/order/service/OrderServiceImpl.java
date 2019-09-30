package com.microservices.order.service;

import com.microservices.order.dto.OrderDto;
import com.microservices.order.dto.OrderItemDto;
import com.microservices.order.entity.OrderEntity;
import com.microservices.order.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    private OrderRepository orderRepository;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository){
        this.orderRepository = orderRepository;
    }

    @Override
    public List<OrderDto> getAllOrders(){

        List<OrderDto> orders = new ArrayList<OrderDto>();
        orderRepository.findAll().forEach(orderEntity ->
                orders.add(OrderServiceImpl.convertToOrderDto(orderEntity)));

        return orders;
    }

    private static OrderDto convertToOrderDto(OrderEntity orderEntity){
        List<OrderItemDto> orderItems = orderEntity.getOrderItemEntities()
                .stream()
                .map(x -> x.toOrderItemDto())
                .collect(Collectors.toCollection(ArrayList::new));

        double totalCost = orderItems
                .stream()
                .mapToDouble(x -> x.getPrice().doubleValue())
                .sum();

        return new OrderDto(
                orderEntity.getId(),
                orderEntity.getOrderStatus(),
                orderEntity.getUserName(),
                BigDecimal.valueOf(totalCost),
                orderItems);
    }
}
