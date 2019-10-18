package com.microservices.item.controller;


import com.microservices.item.dto.ItemCreationDto;
import com.microservices.item.dto.ItemDto;
import com.microservices.item.dto.ItemToReturnDto;
import com.microservices.item.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/item")
public class ItemController {
    private final ItemService itemService;
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

    @PutMapping("{id}/increase/{amount}")
    public ItemDto increaseById(@PathVariable long amount, @PathVariable long id) {
        return itemService.increaseById(id, amount);
    }

    @PutMapping("return-items")
    @ResponseBody
    public String returnToItem(@Valid @RequestBody List<ItemToReturnDto> items) {
        return itemService.returnToItem(items);
    }

    @PutMapping("{id}/decrease/{amount}")
    public ItemDto decreaseById(@PathVariable long amount, @PathVariable long id) {
        return itemService.decreaseById(id, amount);
    }

}
