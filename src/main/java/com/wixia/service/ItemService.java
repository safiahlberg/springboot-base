package com.wixia.service;

import com.wixia.domain.Item;
import com.wixia.domain.ItemRepository;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static com.wixia.configuration.CacheConfig.ITEM_CACHE;

@Service
public class ItemService {

    private final ItemRepository itemRepository;

    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @Cacheable(value = ITEM_CACHE)
    public Optional<Item> findById(long id) {
        return itemRepository.findById(id);
    }

    @Cacheable(value = ITEM_CACHE)
    public List<Item> findAll() {
        return StreamSupport.stream(itemRepository.findAll().spliterator(), false)
            .collect(Collectors.toList());
    }

    @CacheEvict(value = ITEM_CACHE)
    public Item save(Item item) {
        return itemRepository.save(item);
    }
}
