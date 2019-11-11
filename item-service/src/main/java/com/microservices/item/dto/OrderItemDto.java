package com.microservices.item.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@NoArgsConstructor // For Jackson2JsonMessageConverter
@AllArgsConstructor
public class OrderItemDto {

    private int id;

    private int itemId;

    private int amount;

    private String name;

    private BigDecimal price;
}
