package com.microservices.item.service;

import com.microservices.item.dto.ItemCreationDto;
import com.microservices.item.dto.ItemDto;
import com.microservices.item.dto.ItemToReturnDto;
import com.microservices.item.entity.ItemEntity;
import com.microservices.item.repository.ItemRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ItemServiceImpl implements ItemService{

    private static final Logger logger = LoggerFactory.getLogger(ItemServiceImpl.class);

    private ItemRepository itemRepository;

    @Autowired
    public ItemServiceImpl(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @Override
    public List<ItemDto> getItems() {

        List<ItemDto> items = new ArrayList<>();
        itemRepository.findAll().forEach(item -> {
            items.add(convertToItemDto(item));
        });
        logger.info("Returned getItems of all items");
        return items;
    }

    @Override
    public ItemDto getById(long id) {
        ItemEntity item = itemRepository.findById(id).orElseThrow(RuntimeException::new);
        logger.info("Returned item with id = {}", item.getId());
        return convertToItemDto(item);
    }

    @Override
    public ItemDto create(ItemCreationDto itemCreationDto) {
        ItemEntity item = itemRepository.save(
                new ItemEntity(itemCreationDto.getName(), itemCreationDto.getPrice(), itemCreationDto.getAmount()));
        logger.info("Created item with id = {}", item.getId());
        return convertToItemDto(item);
    }

    @Override
    public ItemDto increaseById(long id, long amount) throws RuntimeException {
        ItemEntity item = itemRepository.findById(id).orElseThrow(RuntimeException::new);
        item.setAmount(item.getAmount()+ amount);
        logger.info("item with id = {} amount increased by {}", item.getId(), amount);
        return convertToItemDto(itemRepository.save(item));
    }

    @Override
    public String returnToItem(List<ItemToReturnDto> items) {
        items.forEach(returnItem -> {
            ItemEntity tempItem = itemRepository.findById(returnItem.getId()).orElseThrow(RuntimeException::new);
            tempItem.setAmount(returnItem.getAmount() + tempItem.getAmount());
            itemRepository.save(tempItem);
        });
        return "{\"success\":1}";
    }

    @Override
    public ItemDto decreaseById(long id, long amount) throws RuntimeException {
        ItemEntity item = itemRepository.findById(id).orElseThrow(RuntimeException::new);

        if (amount > item.getAmount()) {
            throw new RuntimeException("Amount can not be negative");
        }
        item.setAmount(item.getAmount() - amount);
        logger.info("item with id = {} amount decreased by {}", item.getId(), amount);
        return convertToItemDto(itemRepository.save(item));
    }

    private ItemDto convertToItemDto(ItemEntity item) {
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getPrice(),
                item.getAmount()
        );
    }
}
