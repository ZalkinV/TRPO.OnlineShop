package com.microservices.item.controller;


import com.microservices.item.entity.ItemEntity;
import com.microservices.item.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/item")
public class ItemController {
    private final ItemService itemService;
    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping
    public List<ItemEntity> list() {
        return itemService.list();
    }

    @GetMapping("{id}")
    public Optional<ItemEntity> getOne(@PathVariable long id){
        return itemService.getOne(id);
    }

    @PostMapping
    public ItemEntity create(@Valid @RequestBody ItemEntity item) {
        return itemService.create(item);
    }

    @PutMapping("{id}/increasing/{amount}")
    public ItemEntity increaseAmount(@PathVariable long amount, @PathVariable long id) {
        return itemService.increaseAmount(id, amount);
    }

    @PutMapping("{id}/decreasing/{amount}")
    public ItemEntity decreaseAmount(@PathVariable long amount, @PathVariable long id) {
        return itemService.decreaseAmount(id, amount);
    }

}
