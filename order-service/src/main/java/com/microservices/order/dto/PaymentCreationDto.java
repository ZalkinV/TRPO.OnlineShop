package com.microservices.order.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PaymentCreationDto {
    Integer orderId;

    CardAuthorizationInfo cardAuthorizationInfo;
}
