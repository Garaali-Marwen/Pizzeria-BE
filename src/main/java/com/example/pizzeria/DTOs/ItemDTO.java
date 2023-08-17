package com.example.pizzeria.DTOs;

import com.example.pizzeria.Entities.Image;
import com.example.pizzeria.Entities.Size;

import java.util.List;

public record ItemDTO (
        Long id,
        String name,
        String description,
        List<ItemIngredientDTO> itemIngredients,
        Image image,
        List<Size> sizes,
        double price
){
}
