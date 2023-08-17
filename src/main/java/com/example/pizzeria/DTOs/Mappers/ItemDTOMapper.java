package com.example.pizzeria.DTOs.Mappers;

import com.example.pizzeria.DTOs.ItemDTO;
import com.example.pizzeria.Entities.Item;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ItemDTOMapper implements Function<Item, ItemDTO> {

    private final ItemIngredientDTOMapper itemIngredientDTOMapper;

    @Override
    public ItemDTO apply(Item item) {
        return new ItemDTO(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getItemIngredients().stream().map(itemIngredientDTOMapper).collect(Collectors.toList()),
                item.getImage(),
                item.getSizes(),
                item.getPrice()
        );
    }
}
