package com.example.pizzeria.DTOs;

public record StockItemDTO (
        Long id,
        ItemDTO item,
        IngredientDTO ingredient,
        float quantity
){
}
