package com.example.pizzeria.DTOs.Mappers;

import com.example.pizzeria.DTOs.ItemDiscountDTO;
import com.example.pizzeria.Entities.ItemDiscount;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
@AllArgsConstructor
public class ItemDiscountDTOMapper implements Function<ItemDiscount, ItemDiscountDTO> {

    private final ItemDTOMapper itemDTOMapper;
    @Override
    public ItemDiscountDTO apply(ItemDiscount itemDiscount) {
        return new ItemDiscountDTO(
                itemDiscount.getId(),
                itemDiscount.getDiscount(),
                itemDTOMapper.apply(itemDiscount.getItem()),
                itemDiscount.getQuantity(),
                itemDiscount.getSize()
        );
    }
}
