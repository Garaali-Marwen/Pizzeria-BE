package com.example.pizzeria.Controllers;

import com.example.pizzeria.DTOs.ItemIngredientDTO;
import com.example.pizzeria.Entities.ItemIngredient;
import com.example.pizzeria.Services.ItemIngredientService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/itemIngredient")
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class ItemIngredientController {

    private final ItemIngredientService itemIngredientService;

    @GetMapping("/all")
    public List<ItemIngredientDTO> getAllItemIngredients() {
        return itemIngredientService.getAllItemIngredients();
    }

    @PostMapping("/add")
    public ItemIngredient addItemIngredient(@RequestBody ItemIngredient itemIngredient) {
        return itemIngredientService.addItemIngredient(itemIngredient);
    }

    @GetMapping("/{id}")
    public ItemIngredientDTO getItemIngredientById(@PathVariable("id") Long itemIngredientId) {
        return itemIngredientService.getItemIngredientById(itemIngredientId);
    }

    @PutMapping("/update")
    public ItemIngredientDTO updateItemIngredient(@RequestBody ItemIngredientDTO itemIngredientDTO) {
        return itemIngredientService.updateItemIngredient(itemIngredientDTO);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteItemIngredient(@PathVariable("id") Long itemIngredientId) {
        itemIngredientService.deleteItemIngredient(itemIngredientId);
    }

    @GetMapping("/{idig}/item/{idit}/ingredient/{idin}")
    public ItemIngredientDTO affectItemAndIngredient(
            @PathVariable("idit") Long itemId, @PathVariable("idin") Long ingredientId, @PathVariable("idig") Long itemIngredientId) {
        return itemIngredientService.affectItemAndIngredient(itemId, ingredientId, itemIngredientId);
    }
}
