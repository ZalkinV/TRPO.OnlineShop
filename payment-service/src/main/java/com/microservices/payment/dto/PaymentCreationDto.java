package com.microservices.payment.dto;

import lombok.Getter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.beans.ConstructorProperties;

@Getter
public class PaymentCreationDto {
    @NotNull
    @Valid
    private Integer orderId;

    @ConstructorProperties({"orderId"})
    public PaymentCreationDto(int orderId) {
        this.orderId = orderId;
    }
}
