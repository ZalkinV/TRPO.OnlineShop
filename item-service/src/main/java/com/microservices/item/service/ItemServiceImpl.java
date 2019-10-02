package com.microservices.item.service;

import com.microservices.item.dto.ItemCreationDto;
import com.microservices.item.dto.ItemDto;
import com.microservices.item.entity.ItemEntity;
import com.microservices.item.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ItemServiceImpl implements ItemService{

    private ItemRepository itemRepository;

    @Autowired
    public ItemServiceImpl(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @Override
    public List<ItemDto> list() {

        List<ItemDto> items = new ArrayList<>();
        itemRepository.findAll().forEach(item -> {
            items.add(convertToItemDto(item));
        });

        return items;
    }

    @Override
    public ItemDto getOne(long id) {
        ItemEntity item = itemRepository.findById(id).orElseThrow(RuntimeException::new);
        return convertToItemDto(item);
    }

    @Override
    public ItemDto create(ItemCreationDto itemCreationDto) {
        return convertToItemDto(itemRepository.save(
                new ItemEntity(itemCreationDto.getName(), itemCreationDto.getPrice(), itemCreationDto.getAmount()))
        );
    }

    @Override
    public ItemDto increaseAmount(long id, long amount) throws RuntimeException {
        ItemEntity item = itemRepository.findById(id).orElseThrow(RuntimeException::new);
        item.setAvailableAmount(item.getAvailableAmount()+ amount);
        return convertToItemDto(itemRepository.save(item));
    }

    @Override
    public ItemDto decreaseAmount(long id, long amount) throws RuntimeException {
        ItemEntity item = itemRepository.findById(id).orElseThrow(RuntimeException::new);
        item.setAvailableAmount(item.getAvailableAmount() - amount);
        return convertToItemDto(itemRepository.save(item));
    }

    private ItemDto convertToItemDto(ItemEntity item) {
        return new ItemDto(
                item.getEntityId(),
                item.getName(),
                item.getPrice(),
                item.getAvailableAmount()
        );
    }
}
