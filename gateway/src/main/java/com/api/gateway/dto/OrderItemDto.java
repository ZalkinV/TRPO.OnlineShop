package com.api.gateway.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class OrderItemDto {

    private int id;

    private int itemId;

    private int amount;

    private String name;

    private BigDecimal price;
}
