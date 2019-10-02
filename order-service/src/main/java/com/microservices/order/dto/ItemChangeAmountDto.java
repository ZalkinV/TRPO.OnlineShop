package com.microservices.order.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class ItemChangeAmountDto {

    private int itemId;

    private int amount;

    private String username;
}
