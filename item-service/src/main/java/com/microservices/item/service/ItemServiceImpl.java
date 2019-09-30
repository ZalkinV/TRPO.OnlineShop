package com.microservices.item.service;

import com.microservices.item.entity.ItemEntity;
import com.microservices.item.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class ItemServiceImpl implements ItemService{

    private ItemRepository itemRepository;

    @Autowired
    public ItemServiceImpl(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @Override
    public List<ItemEntity> list() {
        return (List<ItemEntity>) itemRepository.findAll();
    }

    @Override
    public Optional<ItemEntity> getOne(long id) {
        return itemRepository.findById(id);
    }

    @Override
    public ItemEntity create(ItemEntity item) {
        return itemRepository.save(item);
    }

    @Override
    public ItemEntity increaseAmount(long id, long amount) throws RuntimeException {
        ItemEntity item = itemRepository.findById(id).orElseThrow(RuntimeException::new);
        item.setAvailableAmount(item.getAvailableAmount()+ amount);
        return itemRepository.save(item);
    }

    @Override
    public ItemEntity decreaseAmount(long id, long amount) throws RuntimeException {
        ItemEntity item = itemRepository.findById(id).orElseThrow(RuntimeException::new);
        item.setAvailableAmount(item.getAvailableAmount() - amount);
        return itemRepository.save(item);
    }
}
