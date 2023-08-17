package com.example.pizzeria.Controllers;

import com.example.pizzeria.DTOs.CategoryDTO;
import com.example.pizzeria.Entities.Category;
import com.example.pizzeria.Services.CategoryService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/category")
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping("/add")
    public CategoryDTO addCategory(@RequestBody Category category) {
        return categoryService.addCategory(category);
    }

    @GetMapping("/all")
    public List<CategoryDTO> getAllCategories() {
        return categoryService.getAllCategories();
    }

    @GetMapping("/{id}")
    public CategoryDTO getCategoryById(@PathVariable("id") Long categoryId) {
        return categoryService.getCategoryById(categoryId);
    }

    @PutMapping("/update")
    public CategoryDTO updateCategory(@RequestBody CategoryDTO categoryDTO) {
        return categoryService.updateCategory(categoryDTO);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteCategory(@PathVariable("id") Long categoryId) {
        categoryService.deleteCategory(categoryId);
    }

    @GetMapping("/item/{idi}")
    public CategoryDTO getCategoryByItems_Id(@PathVariable("idi") Long itemId) {
        return categoryService.getCategoryByItems_Id(itemId);
    }
}
