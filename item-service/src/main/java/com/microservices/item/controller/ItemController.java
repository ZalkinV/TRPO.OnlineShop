package com.microservices.item.controller;


import com.microservices.item.dto.ItemCreationDto;
import com.microservices.item.dto.ItemDto;
import com.microservices.item.dto.OrderItemDto;
import com.microservices.item.service.ItemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@EnableRabbit
@RestController
@RequestMapping("/item")
public class ItemController {
    private final ItemService itemService;

    private static final Logger logger = LoggerFactory.getLogger(ItemController.class);


    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping
    public List<ItemDto> list() {
        return itemService.list();
    }

    @GetMapping("{id}")
    public ItemDto getOne(@PathVariable long id){
        return itemService.getOne(id);
    }

    @PostMapping
    public ItemDto create(@Valid @RequestBody ItemCreationDto itemCreationDto) {
        return itemService.create(itemCreationDto);
    }

    @PutMapping("{id}/increasing/{amount}")
    public ItemDto increaseAmount(@PathVariable long amount, @PathVariable long id) {
        return itemService.increaseAmount(id, amount);
    }

    @PutMapping("{id}/decreasing/{amount}")
    public ItemDto decreaseAmount(@PathVariable long amount, @PathVariable long id) {
        return itemService.decreaseAmount(id, amount);
    }

    @RabbitListener(queues = "qitem")
    public void returnItems(List<OrderItemDto> items) {
        logger.info("Received items count=" + items.size());
    }

}
