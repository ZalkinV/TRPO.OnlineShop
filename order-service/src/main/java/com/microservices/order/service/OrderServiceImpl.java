package com.microservices.order.service;

import com.microservices.order.dto.ItemChangeAmountDto;
import com.microservices.order.dto.OrderDto;
import com.microservices.order.dto.OrderItemDto;
import com.microservices.order.entity.OrderEntity;
import com.microservices.order.entity.OrderItemEntity;
import com.microservices.order.entity.OrderStatus;
import com.microservices.order.repository.OrderRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

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

        logger.info("Item was added successfully");

        return orders;
    }

    @Override
    public OrderDto getOrderById(int orderId){
        OrderEntity orderEntity = getExistedOrder(orderId);

        return OrderServiceImpl.convertToDto(orderEntity);
    }

    @Override
    public OrderDto addItemToOrder(int orderId, ItemChangeAmountDto additionDto){
        Optional<OrderEntity> orderOptional = orderRepository.findById(orderId);

        if (orderOptional.isPresent()){
            OrderItemEntity orderItem = orderOptional.get()
                .getOrderItemEntities().stream()
                .filter(oie -> oie.getItemId() == additionDto.getItemId())
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("There is no orderItem with itemId=" + additionDto.getItemId() + " in order with id=" + orderId));

            int newAmount = orderItem.getAmount() + additionDto.getAmount();
            orderItem.setAmount(newAmount);
        } else {
            throw new NotImplementedException();
        }

        logger.info("Adding item to order with id = {}", orderId);
        OrderEntity savedOrder = orderRepository.save(orderOptional.get());
        logger.info("Item was added successfully");

        return OrderServiceImpl.convertToDto(savedOrder);
    }

    @Override
    public OrderDto setOrderStatus(int orderId, OrderStatus orderStatus){
        OrderEntity order = getExistedOrder(orderId);

        order.setOrderStatus(orderStatus);

        OrderEntity savedOrder = orderRepository.save(order);
        logger.info("Order status was changed to {}", orderStatus);

        return OrderServiceImpl.convertToDto(savedOrder);
    }

    private OrderEntity getExistedOrder(int orderId){
        return orderRepository.findById(orderId)
            .orElseThrow(() -> new IllegalArgumentException("There is no order with id=" + orderId));
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
            orderItemEntity.getItemId(),
            orderItemEntity.getAmount(),
            orderItemEntity.getName(),
            orderItemEntity.getPrice());
    }
}
