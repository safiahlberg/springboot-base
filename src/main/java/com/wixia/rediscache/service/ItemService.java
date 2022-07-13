package com.wixia.rediscache.service;

import com.wixia.rediscache.persistence.ItemEo;
import com.wixia.rediscache.persistence.ItemRepository;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class ItemService {

    private final ItemRepository itemRepository;

    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public Optional<ItemEo> findById(long id) {
        return itemRepository.findById(id);
    }

    public List<ItemEo> findAll() {
        return StreamSupport.stream(itemRepository.findAll().spliterator(), false)
            .collect(Collectors.toList());
    }
}
