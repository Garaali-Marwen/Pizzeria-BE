package com.example.pizzeria.DTOs.Mappers;

import com.example.pizzeria.DTOs.ItemIngredientDTO;
import com.example.pizzeria.Entities.ItemIngredient;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
@AllArgsConstructor
public class ItemIngredientDTOMapper implements Function<ItemIngredient, ItemIngredientDTO> {

    private final IngredientDTOMapper ingredientDTOMapper;

    @Override
    public ItemIngredientDTO apply(ItemIngredient itemIngredient) {
        return new ItemIngredientDTO(
                itemIngredient.getId(),
                ingredientDTOMapper.apply(itemIngredient.getIngredient()),
                itemIngredient.getQuantity(),
                itemIngredient.getType(),
                itemIngredient.getPrice()
        );
    }
}
