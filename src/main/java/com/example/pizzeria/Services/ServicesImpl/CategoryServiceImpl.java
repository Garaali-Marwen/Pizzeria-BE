package com.example.pizzeria.Services.ServicesImpl;

import com.example.pizzeria.DTOs.CategoryDTO;
import com.example.pizzeria.DTOs.Mappers.CategoryDTOMapper;
import com.example.pizzeria.Entities.Category;
import com.example.pizzeria.Entities.Item;
import com.example.pizzeria.Repositories.CategoryRepository;
import com.example.pizzeria.Repositories.ItemRepository;
import com.example.pizzeria.Services.CategoryService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryDTOMapper categoryDTOMapper;
    private final ItemRepository itemRepository;

    @Override
    public CategoryDTO addCategory(Category category) {
        return categoryDTOMapper.apply(categoryRepository.save(category));
    }

    @Override
    public List<CategoryDTO> getAllCategories() {
        return categoryRepository.findAll().stream().map(categoryDTOMapper).collect(Collectors.toList());
    }

    @Override
    public CategoryDTO getCategoryById(Long categoryId) {
        return categoryDTOMapper.apply(categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NoSuchElementException("No category found with id: "+categoryId)));
    }

    @Override
    public CategoryDTO updateCategory(CategoryDTO categoryDTO) {
        Category category = categoryRepository.findById(categoryDTO.id())
                .orElseThrow(() -> new NoSuchElementException("No category found with id: "+ categoryDTO.id()));
        category.setName(categoryDTO.name());
        categoryRepository.save(category);
        return categoryDTO;
    }

    @Override
    public void deleteCategory(Long categoryId) {
        categoryRepository.deleteById(categoryId);
    }

    @Override
    public CategoryDTO getCategoryByItems_Id(Long id) {
        return categoryDTOMapper.apply(categoryRepository.getCategoryByItems_Id(id));
    }
}
