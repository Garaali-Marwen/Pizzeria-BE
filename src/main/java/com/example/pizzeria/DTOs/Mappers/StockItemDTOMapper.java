package com.example.pizzeria.DTOs.Mappers;

import com.example.pizzeria.DTOs.StockItemDTO;
import com.example.pizzeria.Entities.StockItem;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
@AllArgsConstructor
public class StockItemDTOMapper implements Function<StockItem, StockItemDTO> {

    private final ItemDTOMapper itemDTOMapper;
    private final IngredientDTOMapper ingredientDTOMapper;

    @Override
    public StockItemDTO apply(StockItem stockItem) {
        if (stockItem.getItem() == null)
            return new StockItemDTO(
                    stockItem.getId(),
                    null,
                    ingredientDTOMapper.apply(stockItem.getIngredient()),
                    stockItem.getQuantity()
            );
        return new StockItemDTO(
                stockItem.getId(),
                itemDTOMapper.apply(stockItem.getItem()),
                null,
                stockItem.getQuantity()
        );
    }
}
