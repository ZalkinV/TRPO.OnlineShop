package com.api.gateway.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@AllArgsConstructor
public class OrderDto {

    private int id;

    private OrderStatus orderStatus;

    private String userName;

    private BigDecimal totalCost;

    private List<OrderItemDto> items;
}
