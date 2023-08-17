package com.example.pizzeria.Controllers;

import com.example.pizzeria.DTOs.IngredientDTO;
import com.example.pizzeria.Entities.Image;
import com.example.pizzeria.Entities.Ingredient;
import com.example.pizzeria.Services.ImageService;
import com.example.pizzeria.Services.IngredientService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/ingredient")
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class IngredientController {

    private final IngredientService ingredientService;
    private final ImageService imageService;

    @GetMapping("/all")
    public List<IngredientDTO> getAllIngredients() {
        return ingredientService.getAllIngredients();
    }

    @GetMapping("/all/{pageNumber}/{pageSize}")
    public Page<IngredientDTO> getAllIngredients(@PathVariable("pageNumber") Integer pageNumber, @PathVariable("pageSize") Integer pageSize) {
        return ingredientService.getAllIngredients(pageNumber, pageSize);
    }

    @PostMapping(value = "/add", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public IngredientDTO addIngredient(@RequestPart("ingredient") Ingredient ingredient,
                                       @RequestPart("image") MultipartFile image) {
        try {
            Image images = imageService.uploadFile(image);
            ingredient.setImage(images);
            return ingredientService.addIngredient(ingredient);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    @GetMapping("/{id}")
    public IngredientDTO getIngredientById(@PathVariable("id") Long ingredientId) {
        return ingredientService.getIngredientById(ingredientId);
    }

    @PutMapping(value = "/update", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public IngredientDTO updateIngredient(@RequestPart("ingredient") IngredientDTO ingredientDTO,
                                          @RequestPart("image") MultipartFile image) {
        try {
            Image images = imageService.uploadFile(image);
            return ingredientService.updateIngredient(ingredientDTO, images);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    @DeleteMapping("/delete/{id}")
    public void deleteIngredient(@PathVariable("id") Long ingredientId) {
        ingredientService.deleteIngredient(ingredientId);
    }
}
