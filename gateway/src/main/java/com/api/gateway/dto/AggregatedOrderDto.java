package com.api.gateway.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AggregatedOrderDto {
    private OrderDto orderDto;
    private PaymentDto paymentDto;
}
