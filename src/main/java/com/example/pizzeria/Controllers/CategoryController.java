package com.example.pizzeria.Controllers;

import com.example.pizzeria.Configuration.CategoryIncomeResponse;
import com.example.pizzeria.DTOs.CategoryDTO;
import com.example.pizzeria.Entities.Category;
import com.example.pizzeria.Entities.Image;
import com.example.pizzeria.Entities.Item;
import com.example.pizzeria.Services.CategoryService;
import com.example.pizzeria.Services.ImageService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/category")
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class CategoryController {

    private final CategoryService categoryService;
    private final ImageService imageService;

    @PostMapping(value = "/add", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public CategoryDTO addCategory(@RequestPart("category") Category category,
                                   @RequestPart("image") MultipartFile image,
                                   @RequestPart("icon") MultipartFile icon) {
        try {
            Image images = imageService.uploadFile(image);
            Image icone = imageService.uploadFile(icon);
            category.setImage(images);
            category.setIcon(icone);
            return categoryService.addCategory(category);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    @GetMapping("/all")
    public List<CategoryDTO> getAllCategories() {
        return categoryService.getAllCategories();
    }

    @GetMapping("/{id}")
    public CategoryDTO getCategoryById(@PathVariable("id") Long categoryId) {
        return categoryService.getCategoryById(categoryId);
    }

    @PutMapping(value = "/update", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public CategoryDTO updateCategory(@RequestPart("category") CategoryDTO category,
                                      @RequestPart("image") MultipartFile image,
                                      @RequestPart("icon") MultipartFile icon) {
        try {
            Image images = imageService.uploadFile(image);
            Image icone = imageService.uploadFile(icon);
            return categoryService.updateCategory(category, images, icone);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    @DeleteMapping("/delete/{id}")
    public void deleteCategory(@PathVariable("id") Long categoryId) {
        categoryService.deleteCategory(categoryId);
    }

    @GetMapping("/item/{idi}")
    public CategoryDTO getCategoryByItems_Id(@PathVariable("idi") Long itemId) {
        return categoryService.getCategoryByItems_Id(itemId);
    }

    @GetMapping("/income/stats/{year}")
    List<CategoryIncomeResponse> getCategoryIncomeForYear(@PathVariable("year") int year) {
        return categoryService.getCategoryIncomeForYear(year);
    }

    @GetMapping("/{categoryId}/order-item-count")
    public Map<String, Object> getOrdersCountByItemAndCategory(@PathVariable("categoryId") Long categoryId) {
        return categoryService.getOrdersCountByItemAndCategory(categoryId);
    }

}
