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

@EnableRabbit
@RestController
public class ItemController {

    
    @Autowired
    private final ItemService itemService;

    private static final Logger logger = LoggerFactory.getLogger(ItemController.class);


    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping
    public List<ItemDto> getItems() {
        return itemService.getItems();
    }

    @GetMapping("{id}")
    public ItemDto getById(@PathVariable long id){
        return itemService.getById(id);
    }

    @PostMapping
    public ItemDto create(@Valid @RequestBody ItemCreationDto itemCreationDto) {
        return itemService.create(itemCreationDto);
    }

    @PutMapping("{id}/increasing/{amount}")
    public ItemDto increaseById(@PathVariable long amount, @PathVariable long id) {
        return itemService.increaseById(id, amount);
    }

    @PutMapping("{id}/decreasing/{amount}")
    public ItemDto decreaseById(@PathVariable long amount, @PathVariable long id) {
        return itemService.decreaseById(id, amount);
    }

    @RabbitListener(queues = "qitem")
    public void returnItems(List<OrderItemDto> items) {
        itemService.returnItems(items);
        logger.info("Received items count=" + items.size());
    }

}
