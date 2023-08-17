package com.example.pizzeria.Services;

import com.example.pizzeria.DTOs.ItemIngredientDTO;
import com.example.pizzeria.Entities.ItemIngredient;

import java.util.List;

public interface ItemIngredientService {

    ItemIngredient addItemIngredient(ItemIngredient itemIngredient);
    List<ItemIngredientDTO> getAllItemIngredients();
    ItemIngredientDTO getItemIngredientById(Long itemIngredientId);
    ItemIngredientDTO updateItemIngredient(ItemIngredientDTO itemIngredientDTO);
    void deleteItemIngredient(Long itemIngredientId);
    ItemIngredientDTO affectItemAndIngredient(Long itemId, Long ingredientId, Long itemIngredientId);
}
