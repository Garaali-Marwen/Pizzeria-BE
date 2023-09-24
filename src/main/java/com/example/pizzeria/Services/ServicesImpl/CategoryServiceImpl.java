package com.example.pizzeria.Services.ServicesImpl;

import com.example.pizzeria.Configuration.CategoryIncomeResponse;
import com.example.pizzeria.DTOs.CategoryDTO;
import com.example.pizzeria.DTOs.ItemDTO;
import com.example.pizzeria.DTOs.Mappers.CategoryDTOMapper;
import com.example.pizzeria.Entities.Category;
import com.example.pizzeria.Entities.Image;
import com.example.pizzeria.Entities.Item;
import com.example.pizzeria.Repositories.CategoryRepository;
import com.example.pizzeria.Repositories.ItemRepository;
import com.example.pizzeria.Services.CategoryService;
import com.example.pizzeria.Services.ImageService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryDTOMapper categoryDTOMapper;
    private final ImageService imageService;

    @Override
    public CategoryDTO addCategory(Category category) {
        return categoryDTOMapper.apply(categoryRepository.save(category));
    }

    @Override
    public List<CategoryDTO> getAllCategories() {
        List<Category> allCategories = categoryRepository.findAll();

        allCategories.sort((c1, c2) -> {
            if (c1.getName().toLowerCase().contains("pizza")) {
                return -1;
            } else if (c1.getName().toLowerCase().contains("boisson")) {
                return 1;
            } else if (c2.getName().toLowerCase().contains("pizza")) {
                return 1;
            } else if (c2.getName().toLowerCase().contains("boisson")) {
                return -1;
            }
            return c1.getName().compareTo(c2.getName());
        });

        return allCategories.stream()
                .map(categoryDTOMapper)
                .collect(Collectors.toList());
    }


    @Override
    public CategoryDTO getCategoryById(Long categoryId) {
        return categoryDTOMapper.apply(categoryRepository.findById(categoryId)
                .orElseThrow(() -> new NoSuchElementException("No category found with id: " + categoryId)));
    }

    @Override
    public CategoryDTO updateCategory(CategoryDTO categoryDTO, Image image, Image icon) {
        Category category = categoryRepository.findById(categoryDTO.id())
                .orElseThrow(() -> new NoSuchElementException("No category found with id: " + categoryDTO.id()));
        Image imageDelete = category.getImage();
        Image iconDelete = category.getIcon();

        category.setName(categoryDTO.name());

        if (image != null)
            category.setImage(image);
        if (icon != null)
            category.setIcon(icon);
        categoryRepository.save(category);
        if (image != null)
            imageService.deleteImage(imageDelete.getId());
        if (icon != null)
            imageService.deleteImage(iconDelete.getId());
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

    @Override
    public List<CategoryIncomeResponse> getCategoryIncomeForYear(int year) {
        List<CategoryIncomeResponse> result = categoryRepository.getCategoryIncomeForYear(year);

        Map<String, List<Double>> categoryDataMap = new HashMap<>();

        for (CategoryIncomeResponse dto : result) {
            String categoryName = dto.getName();
            if (!categoryDataMap.containsKey(categoryName)) {
                categoryDataMap.put(categoryName, new ArrayList<>());
            }
            while (categoryDataMap.get(categoryName).size() < dto.getMonth() - 1) {
                categoryDataMap.get(categoryName).add(0.0); // Fill in missing months with 0 income
            }
            categoryDataMap.get(categoryName).add(dto.getIncome());
        }

        List<CategoryIncomeResponse> finalResult = new ArrayList<>();
        for (String categoryName : categoryDataMap.keySet()) {
            while (categoryDataMap.get(categoryName).size() < 12) {
                categoryDataMap.get(categoryName).add(0.0); // Fill in missing months with 0 income
            }
            CategoryIncomeResponse dto = new CategoryIncomeResponse();
            dto.setName(categoryName);
            dto.setData(categoryDataMap.get(categoryName));
            finalResult.add(dto);
        }

        return finalResult;
    }

    @Override
    public Map<String, Object> getOrdersCountByItemAndCategory(Long categoryId) {
        List<Integer> data = new ArrayList<>();
        List<String> labels = new ArrayList<>();

        for (Object[] row : categoryRepository.getOrdersCountByItemAndCategory(categoryId)) {
            labels.add((String) row[1]); // Item name
            data.add(((Long) row[2]).intValue()); // Order item count
        }

        return Map.of("Data", data, "Labels", labels);
    }

}
