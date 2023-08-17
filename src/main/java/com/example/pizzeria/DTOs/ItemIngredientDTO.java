package com.example.pizzeria.DTOs;

import com.example.pizzeria.Enum.IngredientType;

public record ItemIngredientDTO (
        Long id,
        IngredientDTO ingredient,
        float quantity,
        IngredientType type,
        double price
){
}
