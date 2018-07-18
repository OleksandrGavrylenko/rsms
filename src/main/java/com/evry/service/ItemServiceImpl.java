package com.evry.service;

import com.evry.dto.QuantityDto;
import com.evry.exceptions.ResourceNotFoundException;
import com.evry.model.Category;
import com.evry.model.Item;
import com.evry.repository.CategoryRepository;
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

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public Iterable<Item> findAll() {
        return itemRepository.findAll();
    }

    @Override
    public Item addItem(Long categoryId, Item item) {
        Optional<Category> category = categoryRepository.findById(categoryId);
        if (category.isPresent()) {
            item.setCategory(category.get());
            return itemRepository.save(item);
        } else {
            throw new  ResourceNotFoundException("Category with id: " + categoryId + " not found");
        }
    }

    @Override
    public void updateItem(Long categoryId, Long itemId, Item item) {
        Optional<Category> category = categoryRepository.findById(categoryId);
        if (category.isPresent()) {
            item.setId(itemId);
            itemRepository.save(item);
        } else {
            throw new  ResourceNotFoundException("Category with id: " + categoryId + " not found");
        }
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
    public QuantityDto getQuantity(Long id) {
        Long quantityById = itemRepository.findQuantityById(id);

        return new QuantityDto(id, quantityById);
    }

    @Override
    public void deleteById(Long id) {
        itemRepository.deleteById(id);
    }
}
