package com.api.gateway.feign;

import com.api.gateway.dto.PaymentDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name="payment")
public interface PaymentServiceFeignClient {
    @GetMapping(value = "payment/order/{order_id}")
    PaymentDto getPaymentByOrderId(@PathVariable int order_id);
}
