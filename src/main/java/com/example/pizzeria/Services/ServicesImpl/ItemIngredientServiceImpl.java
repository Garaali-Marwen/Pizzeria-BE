package com.example.pizzeria.Services.ServicesImpl;

import com.example.pizzeria.DTOs.ItemIngredientDTO;
import com.example.pizzeria.DTOs.Mappers.ItemIngredientDTOMapper;
import com.example.pizzeria.Entities.Ingredient;
import com.example.pizzeria.Entities.Item;
import com.example.pizzeria.Entities.ItemIngredient;
import com.example.pizzeria.Repositories.IngredientRepository;
import com.example.pizzeria.Repositories.ItemIngredientRepository;
import com.example.pizzeria.Repositories.ItemRepository;
import com.example.pizzeria.Services.ItemIngredientService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ItemIngredientServiceImpl implements ItemIngredientService {

    private final ItemIngredientRepository itemIngredientRepository;
    private final ItemIngredientDTOMapper itemIngredientDTOMapper;
    private final ItemRepository itemRepository;
    private final IngredientRepository ingredientRepository;

    @Override
    public ItemIngredient addItemIngredient(ItemIngredient itemIngredient) {
        return itemIngredientRepository.save(itemIngredient);
    }

    @Override
    public List<ItemIngredientDTO> getAllItemIngredients() {
        return itemIngredientRepository.findAll().stream().map(itemIngredientDTOMapper).collect(Collectors.toList());
    }

    @Override
    public ItemIngredientDTO getItemIngredientById(Long itemIngredientId) {
        return itemIngredientDTOMapper.apply(itemIngredientRepository.findById(itemIngredientId)
                .orElseThrow(() -> new NoSuchElementException("No itemIngredient found with id: "+itemIngredientId)));
    }

    @Override
    public ItemIngredientDTO updateItemIngredient(ItemIngredientDTO itemIngredientDTO) {
        ItemIngredient itemIngredient = itemIngredientRepository.findById(itemIngredientDTO.id())
                .orElseThrow(() -> new NoSuchElementException("No itemIngredient found with id: "+itemIngredientDTO.id()));
        itemIngredient.setQuantity(itemIngredientDTO.quantity());
        return itemIngredientDTO;
    }

    @Override
    public void deleteItemIngredient(Long itemIngredientId) {
        itemIngredientRepository.deleteById(itemIngredientId);
    }

    @Override
    public ItemIngredientDTO affectItemAndIngredient(Long itemId, Long ingredientId, Long itemIngredientId) {
        ItemIngredient itemIngredient = itemIngredientRepository.findById(itemIngredientId)
                .orElseThrow(() -> new NoSuchElementException("No itemIngredient found with id: "+itemIngredientId));
        Item item = itemRepository.findById(itemIngredientId)
                .orElseThrow(() -> new NoSuchElementException("No item found with id: "+itemId));
        Ingredient ingredient = ingredientRepository.findById(itemIngredientId)
                .orElseThrow(() -> new NoSuchElementException("No ingredient found with id: "+ingredientId));
        itemIngredient.setItem(item);
        itemIngredient.setIngredient(ingredient);
        item.getItemIngredients().add(itemIngredient);
        itemRepository.save(item);
        ingredient.getItemIngredients().add(itemIngredient);
        ingredientRepository.save(ingredient);
        return itemIngredientDTOMapper.apply(itemIngredientRepository.save(itemIngredient));
    }
}
