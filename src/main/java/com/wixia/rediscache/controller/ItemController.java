package com.wixia.rediscache.controller;

import com.wixia.rediscache.persistence.ItemEo;
import com.wixia.rediscache.service.ItemService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ItemController {

    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping("/items")
    public List<ItemEo> findAll() {
        return itemService.findAll();
    }
}
