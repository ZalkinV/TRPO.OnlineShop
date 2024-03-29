package com.microservices.item.service;

import com.microservices.item.dto.ItemCreationDto;
import com.microservices.item.dto.ItemDto;
import com.microservices.item.dto.OrderItemDto;
import com.microservices.item.entity.ItemEntity;

import java.util.List;

public interface ItemService {

    List<ItemDto> getItems();
    ItemDto getById(long id);
    ItemDto create(ItemCreationDto item);
    ItemDto increaseById(long id, long amount) throws RuntimeException;
    ItemDto decreaseById(long id, long amount) throws RuntimeException;
    void returnItems(List<OrderItemDto> items);
}
