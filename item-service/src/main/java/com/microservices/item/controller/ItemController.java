package com.microservices.item.controller;


import com.microservices.item.entity.ItemEntity;
import com.microservices.item.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/item")
public class ItemController {
    private final ItemRepository itemRepository;
    @Autowired
    public ItemController(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @GetMapping
    public List<ItemEntity> list() {
        return itemRepository.findAll();
    }

    @GetMapping("{id}")
    public ItemEntity getOne(@PathVariable("id") ItemEntity item){
        return item;
    }

    @PostMapping
    public ItemEntity create(@Valid @RequestBody ItemEntity item) {
        return itemRepository.save(item);
    }

}
