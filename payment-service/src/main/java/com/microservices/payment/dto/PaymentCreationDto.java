package com.microservices.payment.dto;

import com.microservices.payment.entity.CardAuthorizationInfo;
import lombok.Getter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.beans.ConstructorProperties;

@Getter
public class PaymentCreationDto {
    @NotNull
    @Valid
    private Integer orderId;

    @NotNull
    @Valid
    private CardAuthorizationInfo cardAuthorizationInfo;

    @ConstructorProperties({"orderId", "cardAuthorizationInfo"})
    public PaymentCreationDto(int orderId, CardAuthorizationInfo cardAuthorizationInfo) {
        this.cardAuthorizationInfo = cardAuthorizationInfo;
        this.orderId = orderId;
    }
}
