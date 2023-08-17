package com.example.pizzeria.Configuration;

import com.example.pizzeria.Entities.OrderItem;

import java.util.List;

public class ItemAvailabilityRequest {

    public OrderItem orderItem;
    public List<Long> ingredients;
    public List<OrderItem> orderItems;
}
