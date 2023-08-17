package com.example.pizzeria.Repositories;

import com.example.pizzeria.Entities.ItemIngredient;
import com.example.pizzeria.Enum.IngredientType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemIngredientRepository extends JpaRepository<ItemIngredient, Long> {

    ItemIngredient getItemIngredientByIngredient_IdAndItem_IdAndType(Long ingredientId, Long itemId, IngredientType ingredientType);
    ItemIngredient getItemIngredientByIngredient_IdAndItem_Id(Long ingredientId, Long itemId);

}
