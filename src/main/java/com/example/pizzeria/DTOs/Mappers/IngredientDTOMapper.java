package com.example.pizzeria.DTOs.Mappers;

import com.example.pizzeria.DTOs.IngredientDTO;
import com.example.pizzeria.Entities.Ingredient;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class IngredientDTOMapper implements Function<Ingredient, IngredientDTO> {
    @Override
    public IngredientDTO apply(Ingredient ingredient) {
        return new IngredientDTO(
                ingredient.getId(),
                ingredient.getName(),
                ingredient.getImage(),
                ingredient.getUnit()
        );
    }
}
