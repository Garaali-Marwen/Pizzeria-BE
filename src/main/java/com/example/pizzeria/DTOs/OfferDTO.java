package com.example.pizzeria.DTOs;


import com.example.pizzeria.Entities.Image;

import java.util.Date;
import java.util.List;

public record OfferDTO (
        Long id,
        String description,
        double totalPrice,
        Date beginDate,
        Date endDate,
        List<ItemDiscountDTO> itemsDiscount,
        Image image
){
}
