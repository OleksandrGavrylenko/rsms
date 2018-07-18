package com.evry.service;

import com.evry.exceptions.ResourceNotFoundException;
import com.evry.model.Item;
import com.evry.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class ItemServiceImpl implements ItemService {

    @Autowired
    private ItemRepository itemRepository;

    @Override
    public Iterable<Item> findAll() {
        return itemRepository.findAll();
    }

    @Override
    public Item addItem(Item item) {
        return itemRepository.save(item);
    }

    @Override
    public void updateItem(Long itemId, Item item) {
        item.setId(itemId);
        itemRepository.updateItem(itemId, item.getName(), item.getPrice(), item.getQuantity(), item.getCategory());
    }

    @Override
    public Item findById(Long id) {
        Optional<Item> i = itemRepository.findById(id);
        if (i.isPresent()) {
            return i.get();
        } else {
            throw new ResourceNotFoundException("Item with id: " + id + " not found");
        }
    }

    @Override
    public void deleteById(Long id) {
        itemRepository.deleteById(id);
    }
}
