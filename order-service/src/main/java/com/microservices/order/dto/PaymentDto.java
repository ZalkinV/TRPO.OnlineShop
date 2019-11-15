package com.microservices.order.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@NoArgsConstructor
@Getter
@AllArgsConstructor
public class PaymentDto {
    @NotNull
    private Integer id;

    @NotNull
    private Integer orderId;

    @NotNull
    private PaymentStatus status;
}
