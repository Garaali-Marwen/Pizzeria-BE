package com.example.pizzeria.Services.ServicesImpl;

import com.example.pizzeria.DTOs.ItemDTO;
import com.example.pizzeria.DTOs.Mappers.ItemDTOMapper;
import com.example.pizzeria.Entities.Image;
import com.example.pizzeria.Entities.Item;
import com.example.pizzeria.Entities.ItemIngredient;
import com.example.pizzeria.Entities.Size;
import com.example.pizzeria.Repositories.ItemIngredientRepository;
import com.example.pizzeria.Repositories.ItemRepository;
import com.example.pizzeria.Services.ImageService;
import com.example.pizzeria.Services.ItemService;
import com.example.pizzeria.Services.SizeService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final ItemDTOMapper itemDTOMapper;
    private final SizeService sizeService;
    private final ItemIngredientRepository itemIngredientRepository;
    private final ImageService imageService;

    @Override
    public ItemDTO addItem(Item item) {
        List<Size> sizes = new ArrayList<>();
        for (Size size : item.getSizes()) {
            sizes.add(sizeService.addSize(size));
        }
        item.setSizes(sizes);
        return itemDTOMapper.apply(itemRepository.save(item));
    }

    @Override
    public Page<ItemDTO> getAllItems(Integer pageNumber, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return itemRepository.findAll(pageable).map(itemDTOMapper);
    }

    @Override
    public ItemDTO getItemById(Long itemId) {
        return itemDTOMapper.apply(itemRepository.findById(itemId)
                .orElseThrow(() -> new NoSuchElementException("No item found with id: " + itemId)));
    }

    @Override
    public ItemDTO updateItem(Item item, Image image) {
        Item itemUpdated = itemRepository.findById(item.getId())
                .orElseThrow(() -> new NoSuchElementException("No item found with id: " + item.getId()));

        Image imageDelete = itemUpdated.getImage();
        List<Size> sizeListDelete = new ArrayList<>();
        for (Size size : itemUpdated.getSizes()) {
            if (!item.getSizes().contains(size)) {
                sizeListDelete.add(size);
            }
        }
        List<Size> sizes = new ArrayList<>();
        for (Size size : item.getSizes()) {
            sizes.add(sizeService.addSize(size));
        }

        itemUpdated.setSizes(sizes);
        itemUpdated.setDescription(item.getDescription());
        itemUpdated.setName(item.getName());
        itemUpdated.setPrice(item.getPrice());
        List<ItemIngredient> itemIngredients = item.getItemIngredients();

        for (ItemIngredient itemIngredient : item.getItemIngredients()) {
            itemIngredient.setItem(itemUpdated);
            itemIngredients.set(item.getItemIngredients().indexOf(itemIngredient), itemIngredientRepository.save(itemIngredient));
        }

        for (ItemIngredient itemIngredient : itemUpdated.getItemIngredients()) {
            if (!item.getItemIngredients().contains(itemIngredient)) {
                itemIngredient.setItem(null);
                itemIngredient.setIngredient(null);
                itemIngredientRepository.save(itemIngredient);
                itemIngredientRepository.deleteById(itemIngredient.getId());
            }
        }

        itemUpdated.setItemIngredients(itemIngredients);
        if (image != null)
            itemUpdated.setImage(image);
        ItemDTO itemDTOUpdated = itemDTOMapper.apply(itemRepository.save(itemUpdated));

        sizeListDelete.forEach(size -> sizeService.deleteSize(size.getId()));
        if (image != null)
            imageService.deleteImage(imageDelete.getId());
        return itemDTOUpdated;
    }

    @Override
    public void deleteItem(Long itemId) {
        itemRepository.deleteById(itemId);
    }

    @Override
    public List<ItemDTO> getItemsByCategory_Name(String name) {
        return itemRepository.getItemsByCategory_Name(name).stream().map(itemDTOMapper).collect(Collectors.toList());
    }
}
