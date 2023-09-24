package com.example.pizzeria.DTOs.Mappers;

import com.example.pizzeria.DTOs.CategoryDTO;
import com.example.pizzeria.Entities.Category;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CategoryDTOMapper implements Function<Category, CategoryDTO> {

    private final ItemDTOMapper itemDTOMapper;

    @Override
    public CategoryDTO apply(Category category) {
        return new CategoryDTO(
                category.getId(),
                category.getName(),
                category.getItems().stream().map(itemDTOMapper).collect(Collectors.toList()),
                category.getImage(),
                category.getIcon()
        );
    }
}
