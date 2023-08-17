package com.example.pizzeria.DTOs.Mappers;

import com.example.pizzeria.DTOs.OrderItemDTO;
import com.example.pizzeria.Entities.OrderItem;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class OrderItemDTOMapper implements Function<OrderItem, OrderItemDTO> {

    private ItemDTOMapper itemDTOMapper;
    private IngredientDTOMapper ingredientDTOMapper;

    @Override
    public OrderItemDTO apply(OrderItem orderItem) {
        return new OrderItemDTO(
                orderItem.getId(),
                itemDTOMapper.apply(orderItem.getItem()),
                orderItem.getIngredients().stream().map(ingredientDTOMapper).collect(Collectors.toList()),
                orderItem.getQuantity(),
                orderItem.getSize(),
                orderItem.getPrice()
        );
    }

}
