package com.example.pizzeria.Services;

import com.example.pizzeria.DTOs.IngredientDTO;
import com.example.pizzeria.Entities.Image;
import com.example.pizzeria.Entities.Ingredient;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IngredientService {

    IngredientDTO addIngredient(Ingredient ingredient);
    Page<IngredientDTO> getAllIngredients(Integer pageNumber, Integer pageSize);
    List<IngredientDTO> getAllIngredients();

    IngredientDTO getIngredientById(Long ingredientId);
    IngredientDTO updateIngredient(IngredientDTO ingredientDTO, Image image);
    void deleteIngredient(Long ingredientId);
}
