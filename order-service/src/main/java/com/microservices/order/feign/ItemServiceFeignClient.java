package com.microservices.order.feign;

import com.microservices.order.dto.ItemDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@FeignClient(name="item")
public interface ItemServiceFeignClient {
    @PutMapping("items/{id}/decreasing/{amount}")
    ItemDto decreaseById(@PathVariable long amount, @PathVariable long id);

    @GetMapping("items/{id}")
    ItemDto getById(@PathVariable long id);
}
