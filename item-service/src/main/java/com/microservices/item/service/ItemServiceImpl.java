package com.microservices.item.service;

import com.microservices.item.dto.ItemCreationDto;
import com.microservices.item.dto.ItemDto;
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
    public List<ItemDto> list() {

        List<ItemDto> items = new ArrayList<>();
        itemRepository.findAll().forEach(item -> {
            items.add(convertToItemDto(item));
        });
        logger.info("Returned list of all items");
        return items;
    }

    @Override
    public ItemDto getOne(long id) {
        ItemEntity item = itemRepository.findById(id).orElseThrow(RuntimeException::new);
        logger.info("Returned item with id = {}", item.getEntityId());
        return convertToItemDto(item);
    }

    @Override
    public ItemDto create(ItemCreationDto itemCreationDto) {
        ItemEntity item = itemRepository.save(
                new ItemEntity(itemCreationDto.getName(), itemCreationDto.getPrice(), itemCreationDto.getAmount()));
        logger.info("Created item with id = {}", item.getEntityId());
        return convertToItemDto(item)
        ;
    }

    @Override
    public ItemDto increaseAmount(long id, long amount) throws RuntimeException {
        ItemEntity item = itemRepository.findById(id).orElseThrow(RuntimeException::new);
        item.setAvailableAmount(item.getAvailableAmount()+ amount);
        logger.info("item with id = {} amount increased by {}", item.getEntityId(), amount);
        return convertToItemDto(itemRepository.save(item));
    }

    @Override
    public ItemDto decreaseAmount(long id, long amount) throws RuntimeException {
        ItemEntity item = itemRepository.findById(id).orElseThrow(RuntimeException::new);
        item.setAvailableAmount(item.getAvailableAmount() - amount);
        logger.info("item with id = {} amount decreased by {}", item.getEntityId(), amount);
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
