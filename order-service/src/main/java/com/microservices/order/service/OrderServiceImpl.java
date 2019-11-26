package com.microservices.order.service;

import com.microservices.order.dto.ItemChangeAmountDto;
import com.microservices.order.dto.ItemDto;
import com.microservices.order.dto.OrderDto;
import com.microservices.order.dto.OrderItemDto;
import com.microservices.order.entity.OrderEntity;
import com.microservices.order.entity.OrderItemEntity;
import com.microservices.order.entity.OrderStatus;
import com.microservices.order.feign.ItemServiceFeignClient;
import com.microservices.order.repository.OrderRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Arrays;
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
        OrderEntity orderEntity = this.getOrder(orderId);

        return OrderServiceImpl.convertToDto(orderEntity);
    }

    @Override
    public OrderDto addItemToOrder(int orderId, ItemChangeAmountDto additionDto){
        OrderEntity order = this.getOrder(orderId);

        Optional<OrderItemEntity> optionalOrderItem = order.getOrderItemEntities().stream()
            .filter(oie -> oie.getItemId() == additionDto.getItemId())
            .findFirst();

        OrderItemEntity orderItem;
        if (optionalOrderItem.isPresent()) {
            orderItem = optionalOrderItem.get();
            int newAmount = orderItem.getAmount() + additionDto.getAmount();
            orderItem.setAmount(newAmount);
        } else {
            orderItem = createNewOrderItemEntity(order, additionDto);
            order.getOrderItemEntities().add(orderItem);
        }

        logger.info("Adding item (id={}) to order (id={})", orderItem.getItemId(), orderId);
        OrderEntity savedOrder = orderRepository.save(order);
        logger.info("Item was added successfully");

        return OrderServiceImpl.convertToDto(savedOrder);
    }

    @Override
    public OrderDto setOrderStatus(int orderId, OrderStatus newOrderStatus){
        OrderEntity order = this.getOrder(orderId);
        OrderStatus orderStatus = order.getOrderStatus();

        String wrongStatusChangeMessage = "Status of order (id=" + orderId + ") cannot be changed from " + orderStatus + " to " + newOrderStatus;
        switch (newOrderStatus)
        {
            case PAID:
            case FAILED: {
                if (orderStatus == OrderStatus.COLLECTING)
                    order.setOrderStatus(newOrderStatus);
                else
                    throw new IllegalStateException(wrongStatusChangeMessage);
                break;
            }

            case COMPLETE:
            case CANCELED: {
                if (orderStatus == OrderStatus.PAID)
                    order.setOrderStatus(newOrderStatus);
                else
                    throw new IllegalStateException(wrongStatusChangeMessage);
                break;
            }

            default:
                throw new IllegalStateException(wrongStatusChangeMessage);
        }
        order.setOrderStatus(newOrderStatus);

        OrderEntity savedOrder = orderRepository.save(order);
        logger.info("Order (id={}) status was changed: {} -> {}", orderId, orderStatus, savedOrder.getOrderStatus());

        return OrderServiceImpl.convertToDto(savedOrder);
    }

    @Autowired
    ItemServiceFeignClient itemServiceFeignClient;

    private OrderItemEntity createNewOrderItemEntity(OrderEntity order, ItemChangeAmountDto additionDto) {
        OrderItemEntity orderItemEntity = new OrderItemEntity();
        int id = additionDto.getItemId();
        orderItemEntity.setAmount(additionDto.getAmount());
        orderItemEntity.setItemId(id);
        orderItemEntity.setOrderEntity(order);

        ItemDto itemDto = itemServiceFeignClient.getById(id);
        orderItemEntity.setName(itemDto.getName());
        orderItemEntity.setPrice(itemDto.getPrice());

        return orderItemEntity;
    }

    public OrderDto createNewOrder(ItemChangeAmountDto additionDto) {
        OrderEntity order = new OrderEntity(additionDto.getUsername());
        OrderItemEntity orderItem = createNewOrderItemEntity(order, additionDto);
        order.getOrderItemEntities().add(orderItem);

        OrderEntity savedOrder = orderRepository.save(order);
        logger.info("Item (id={}) was added successfully to order (id={})", orderItem.getItemId(), order.getId());

        return OrderServiceImpl.convertToDto(savedOrder);
    }

    private OrderEntity getOrder(int orderId){
        return orderRepository.findById(orderId)
            .orElseThrow(() -> new IllegalArgumentException("There is no order with id=" + orderId));
    }

    private static OrderDto convertToDto(OrderEntity orderEntity){
        List<OrderItemDto> orderItems = orderEntity
            .getOrderItemEntities().stream()
            .map(OrderServiceImpl::convertToDto)
            .collect(Collectors.toList());

        double totalCost = orderItems.stream()
            .mapToDouble(x -> x.getPrice().doubleValue() * x.getAmount())
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
