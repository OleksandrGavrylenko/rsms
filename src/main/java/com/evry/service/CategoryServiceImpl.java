package com.evry.service;

import com.evry.exceptions.ResourceNotFoundException;
import com.evry.model.Category;
import com.evry.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public Category findById(Long categoryId) {
        Optional<Category> category = categoryRepository.findById(categoryId);
        if (category.isPresent()) {
            return category.get();
        } else {
            throw new ResourceNotFoundException("Category with id: " + categoryId + " not found");
        }
    }
}
