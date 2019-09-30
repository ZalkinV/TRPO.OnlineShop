package com.microservices.order.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class OrderItemDto {

    private int id;

    private int amount;

    private String name;

    private BigDecimal price;
}
