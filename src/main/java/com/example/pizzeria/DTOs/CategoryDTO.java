package com.example.pizzeria.DTOs;

import com.example.pizzeria.Entities.Image;

import java.util.List;

public record CategoryDTO (
        Long id,
        String name,
        List<ItemDTO> items,
        Image image,
        Image icon
){
}
