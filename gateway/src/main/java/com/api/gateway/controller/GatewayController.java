package com.api.gateway.controller;

import com.api.gateway.dto.AggregatedOrderDto;
import com.api.gateway.dto.OrderDto;
import com.api.gateway.dto.PaymentDto;
import com.api.gateway.feign.OrderServiceFeignClient;
import com.api.gateway.feign.PaymentServiceFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("gateway")
public class GatewayController {
    @Autowired
    private OrderServiceFeignClient orderServiceFeignClient;

    @Autowired
    private PaymentServiceFeignClient paymentServiceFeignClient;

    @GetMapping(value = "{orderId}")
    public AggregatedOrderDto getAggregatedOrder(@PathVariable int orderId) {
        OrderDto orderDto = orderServiceFeignClient.getOrderById(orderId);
        PaymentDto paymentDto = paymentServiceFeignClient.getPaymentByOrderId(orderId);

        AggregatedOrderDto aggregatedOrderDto = new AggregatedOrderDto(orderDto, paymentDto);
        return aggregatedOrderDto;
    }
}
