package com.example.pizzeria.DTOs;

import java.util.List;

public record CategoryDTO (
        Long id,
        String name,
        List<ItemDTO> items
){
}
