package com.microservices.order.feign;

import com.microservices.order.dto.PaymentCreationDto;
import com.microservices.order.dto.PaymentDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name="payment")
public interface PaymentServiceFeignClient {
    @PostMapping("payment")
    PaymentDto performPayment(@RequestBody PaymentCreationDto paymentCreationDto);
}
