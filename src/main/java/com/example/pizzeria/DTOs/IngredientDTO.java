package com.example.pizzeria.DTOs;

import com.example.pizzeria.Entities.Image;
import com.example.pizzeria.Enum.Unit;

public record IngredientDTO(
        Long id,
        String name,
        Image image,
        Unit unit
) {
}
