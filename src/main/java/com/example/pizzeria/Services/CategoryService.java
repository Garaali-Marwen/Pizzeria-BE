package com.example.pizzeria.Services;

import com.example.pizzeria.DTOs.CategoryDTO;
import com.example.pizzeria.Entities.Category;

import java.util.List;

public interface CategoryService {

    CategoryDTO addCategory(Category category);
    List<CategoryDTO> getAllCategories();
    CategoryDTO getCategoryById(Long categoryId);
    CategoryDTO updateCategory(CategoryDTO categoryDTO);
    void deleteCategory(Long categoryId);
    CategoryDTO getCategoryByItems_Id(Long id);

}
