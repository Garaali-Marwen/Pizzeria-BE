package com.example.pizzeria.Services.ServicesImpl;

import com.example.pizzeria.DTOs.IngredientDTO;
import com.example.pizzeria.DTOs.Mappers.IngredientDTOMapper;
import com.example.pizzeria.Entities.Image;
import com.example.pizzeria.Entities.Ingredient;
import com.example.pizzeria.Repositories.IngredientRepository;
import com.example.pizzeria.Services.ImageService;
import com.example.pizzeria.Services.IngredientService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class IngredientServiceImpl implements IngredientService {

    private final IngredientRepository ingredientRepository;
    private final IngredientDTOMapper ingredientDTOMapper;
    private final ImageService imageService;
    @Override
    public IngredientDTO addIngredient(Ingredient ingredient) {
        return ingredientDTOMapper.apply(ingredientRepository.save(ingredient));
    }

    @Override
    public Page<IngredientDTO> getAllIngredients(Integer pageNumber, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return ingredientRepository.findAll(pageable).map(ingredientDTOMapper);
    }

    @Override
    public List<IngredientDTO> getAllIngredients() {
        return ingredientRepository.findAll().stream().map(ingredientDTOMapper).collect(Collectors.toList());
    }

    @Override
    public IngredientDTO getIngredientById(Long ingredientId) {
        return ingredientDTOMapper.apply(ingredientRepository.findById(ingredientId)
                .orElseThrow(() -> new NoSuchElementException("No ingredient found with id: "+ingredientId)));
    }

    @Override
    public IngredientDTO updateIngredient(IngredientDTO ingredientDTO, Image image) {
        Ingredient ingredient = ingredientRepository.findById(ingredientDTO.id())
                .orElseThrow(() -> new NoSuchElementException("No ingredient found with id: "+ingredientDTO.id()));
        Image imageDelete = ingredient.getImage();
        if (image != null)
            ingredient.setImage(image);
        ingredient.setName(ingredientDTO.name());
        ingredient.setUnit(ingredientDTO.unit());
        ingredientRepository.save(ingredient);

        if (image != null)
            imageService.deleteImage(imageDelete.getId());
        return ingredientDTO;
    }

    @Override
    public void deleteIngredient(Long ingredientId) {
        ingredientRepository.deleteById(ingredientId);
    }
}
