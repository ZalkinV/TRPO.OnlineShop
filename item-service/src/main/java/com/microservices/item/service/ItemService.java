package com.microservices.item.service;

import com.microservices.item.entity.ItemEntity;

import java.util.List;
import java.util.Optional;

public interface ItemService {

    List<ItemEntity> list();
    Optional<ItemEntity> getOne(long id);
    ItemEntity create(ItemEntity item);
    ItemEntity increaseAmount(long id, long amount) throws RuntimeException;
    ItemEntity decreaseAmount(long id, long amount) throws RuntimeException;

}
