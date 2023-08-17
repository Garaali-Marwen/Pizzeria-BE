package com.example.pizzeria.DTOs;

import com.example.pizzeria.Entities.Address;
import com.example.pizzeria.Enum.OrderState;
import com.example.pizzeria.Enum.OrderType;

import java.util.Date;
import java.util.List;

public record OrderDTO(
        Long id,
        Date date,
        ClientDTO client,
        List<OrderItemDTO> orderItems,
        OrderType orderType,
        double price,
        OrderState state,
        String comment,
        Address address
) {
}
