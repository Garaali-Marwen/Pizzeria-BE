package com.example.pizzeria.Controllers;

import com.example.pizzeria.DTOs.ItemDiscountDTO;
import com.example.pizzeria.Entities.ItemDiscount;
import com.example.pizzeria.Services.ItemDiscountService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/itemDiscount")
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class ItemDicountController {

    private final ItemDiscountService itemDiscountService;

    @GetMapping("/all")
    public List<ItemDiscountDTO> getAllItemDiscounts(){
        return itemDiscountService.getAllItemDiscounts();
    }
    @PostMapping("/add")
    public ItemDiscount addItemDiscount(@RequestBody ItemDiscount itemDiscount){
        return itemDiscountService.addItemDiscount(itemDiscount);
    }
    @GetMapping("/{id}")
    public ItemDiscountDTO getItemDiscountById(@PathVariable("id") Long itemDiscountId){
        return itemDiscountService.getItemDiscountById(itemDiscountId);
    }
    @PutMapping("/update")
    public ItemDiscountDTO updateItemDiscount(@RequestBody ItemDiscountDTO itemDiscountDTO){
        return itemDiscountService.updateItemDiscount(itemDiscountDTO);
    }
    @DeleteMapping("/delete/{id}")
    public void deleteItemDiscount(@PathVariable("id") Long itemDiscountId){
        itemDiscountService.deleteItemDiscount(itemDiscountId);
    }
}
