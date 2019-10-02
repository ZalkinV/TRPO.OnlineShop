package com.microservices.order.service;

import com.microservices.order.dto.OrderDto;
import com.microservices.order.dto.OrderItemDto;
import com.microservices.order.entity.OrderEntity;
import com.microservices.order.entity.OrderItemEntity;
import com.microservices.order.repository.OrderRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private OrderRepository orderRepository;

    @Override
    public List<OrderDto> getAllOrders(){
        List<OrderDto> orders = orderRepository
            .findAll().stream()
            .map(OrderServiceImpl::convertToDto)
            .collect(Collectors.toList());

        return orders;
    }

    private static OrderDto convertToDto(OrderEntity orderEntity){
        List<OrderItemDto> orderItems = orderEntity
            .getOrderItemEntities().stream()
            .map(OrderServiceImpl::convertToDto)
            .collect(Collectors.toList());

        double totalCost = orderItems.stream()
            .mapToDouble(x -> x.getPrice().doubleValue())
            .sum();

        return new OrderDto(
            orderEntity.getId(),
            orderEntity.getOrderStatus(),
            orderEntity.getUserName(),
            BigDecimal.valueOf(totalCost),
            orderItems);
    }

    private static OrderItemDto convertToDto(OrderItemEntity orderItemEntity){
        return new OrderItemDto(
            orderItemEntity.getId(),
            orderItemEntity.getAmount(),
            orderItemEntity.getName(),
            orderItemEntity.getPrice());
    }
}
