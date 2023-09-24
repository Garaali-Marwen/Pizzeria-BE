package com.example.pizzeria.Services;

import com.example.pizzeria.Configuration.CategoryIncomeResponse;
import com.example.pizzeria.DTOs.CategoryDTO;
import com.example.pizzeria.Entities.Category;
import com.example.pizzeria.Entities.Image;

import java.util.List;
import java.util.Map;

public interface CategoryService {

    CategoryDTO addCategory(Category category);

    List<CategoryDTO> getAllCategories();

    CategoryDTO getCategoryById(Long categoryId);

    CategoryDTO updateCategory(CategoryDTO categoryDTO, Image image, Image icon);

    void deleteCategory(Long categoryId);

    CategoryDTO getCategoryByItems_Id(Long id);

    List<CategoryIncomeResponse> getCategoryIncomeForYear(int year);

    Map<String, Object> getOrdersCountByItemAndCategory(Long categoryId);
}
