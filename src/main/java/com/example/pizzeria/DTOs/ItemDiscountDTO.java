package com.example.pizzeria.DTOs;

import com.example.pizzeria.Entities.Size;

public record ItemDiscountDTO (
        Long id,
        double discount,
        ItemDTO item,
        int quantity,
        Size size

){
}
