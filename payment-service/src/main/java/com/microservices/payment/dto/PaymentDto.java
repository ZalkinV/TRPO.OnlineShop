package com.microservices.payment.dto;

import com.microservices.payment.entity.PaymentStatus;
import lombok.*;

import javax.validation.constraints.NotNull;

@NoArgsConstructor
@Getter
@Setter
@ToString
@AllArgsConstructor
public class PaymentDto {
    @NotNull
    private Integer id;

    @NotNull
    private Integer orderId;

    @NotNull
    private PaymentStatus status;

}
