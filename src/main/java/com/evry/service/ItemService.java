package com.evry.service;

import com.evry.dto.QuantityDto;
import com.evry.model.Item;

public interface ItemService {
    Iterable<Item> findAll();
    Item findById(Long id);
    QuantityDto getQuantity(Long id);
    void deleteById(Long id);
    Item addItem(Long categoryId, Item item);
    void updateItem(Long categoryId, Long itemId, Item item);
}
