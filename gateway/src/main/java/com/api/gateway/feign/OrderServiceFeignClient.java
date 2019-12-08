package com.api.gateway.feign;

import com.api.gateway.dto.OrderDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name="order")
public interface OrderServiceFeignClient {
    @GetMapping(value = "order/{orderId}")
    OrderDto getOrderById(@PathVariable int orderId);
}
