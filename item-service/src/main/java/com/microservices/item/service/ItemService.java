package com.microservices.item.service;

import com.microservices.item.dto.ItemCreationDto;
import com.microservices.item.dto.ItemDto;
import com.microservices.item.entity.ItemEntity;

import java.util.List;

public interface ItemService {

    List<ItemDto> list();
    ItemDto getOne(long id);
    ItemDto create(ItemCreationDto item);
    ItemDto increaseAmount(long id, long amount) throws RuntimeException;
    ItemDto decreaseAmount(long id, long amount) throws RuntimeException;

}
