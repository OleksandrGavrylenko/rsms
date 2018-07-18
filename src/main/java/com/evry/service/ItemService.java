package com.evry.service;

import com.evry.model.Item;

public interface ItemService {
    Iterable<Item> findAll();
    Item findById(Long id);
    void deleteById(Long id);
    Item addItem(Item item);
    void updateItem(Long itemId, Item item);
}
