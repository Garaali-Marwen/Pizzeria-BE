package com.example.pizzeria.Configuration;

import com.example.pizzeria.Entities.Ingredient;

import java.util.List;

public class ItemAvailabilityResponse {

    public float quantity;
    public List<Long> unavailableIngredients;
    public float requestedQuantity;
    public boolean unavailable;


    public ItemAvailabilityResponse(float quantity, List<Long> unavailableIngredients, float requestedQuantity, boolean unavailable) {
        this.quantity = quantity;
        this.unavailableIngredients = unavailableIngredients;
        this.requestedQuantity = requestedQuantity;
        this.unavailable = unavailable;
    }
}
