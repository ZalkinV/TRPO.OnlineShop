package com.microservices.item.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public class ItemToReturnDto {

    private Long id;
    private Long amount;
}