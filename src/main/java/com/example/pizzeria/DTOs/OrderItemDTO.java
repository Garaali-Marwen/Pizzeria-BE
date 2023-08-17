package com.example.pizzeria.DTOs;
import java.util.List;

public record OrderItemDTO(
        Long id,
        ItemDTO item,
        List<IngredientDTO> ingredients,
        int quantity,
        String size,
        double price
) {
}
