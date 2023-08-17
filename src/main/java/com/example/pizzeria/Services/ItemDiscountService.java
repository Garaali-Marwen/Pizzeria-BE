package com.example.pizzeria.Services;

import com.example.pizzeria.DTOs.ItemDiscountDTO;
import com.example.pizzeria.Entities.ItemDiscount;

import java.util.List;

public interface ItemDiscountService {

    ItemDiscount addItemDiscount(ItemDiscount itemDiscount);
    List<ItemDiscountDTO> getAllItemDiscounts();
    ItemDiscountDTO getItemDiscountById(Long itemDiscountId);
    ItemDiscountDTO updateItemDiscount(ItemDiscountDTO itemDiscountDTO);
    void deleteItemDiscount(Long itemDiscountId);
}
