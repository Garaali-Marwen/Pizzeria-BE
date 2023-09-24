package com.example.pizzeria.Services;

import com.example.pizzeria.DTOs.ItemDTO;
import com.example.pizzeria.Entities.Image;
import com.example.pizzeria.Entities.Item;
import org.springframework.data.domain.Page;

import java.util.List;


public interface ItemService {

    ItemDTO addItem(Item item);
    Page<ItemDTO> getAllItems(Integer pageNumber, Integer pageSize);
    List<ItemDTO> findAllItems();
    ItemDTO getItemById(Long itemId);
    ItemDTO updateItem(Item item, Image image);
    void deleteItem(Long itemId);
    List<ItemDTO> getItemsByItemIngredientsIsNull();
}
