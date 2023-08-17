package com.example.pizzeria.DTOs;

public record ItemDiscountDTO (
        Long id,
        double discount,
        ItemDTO item
){
}
