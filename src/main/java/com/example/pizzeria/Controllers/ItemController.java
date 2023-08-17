package com.example.pizzeria.Controllers;

import com.example.pizzeria.DTOs.ItemDTO;
import com.example.pizzeria.Entities.Image;
import com.example.pizzeria.Entities.Item;
import com.example.pizzeria.Services.ImageService;
import com.example.pizzeria.Services.ItemService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/item")
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class ItemController {

    private final ItemService itemService;
    private final ImageService imageService;
    private final SimpMessagingTemplate messagingTemplate;

    @GetMapping("/all/{pageNumber}/{pageSize}")
    public Page<ItemDTO> getAllItems(@PathVariable("pageNumber") Integer pageNumber, @PathVariable("pageSize") Integer pageSize) {
        return itemService.getAllItems(pageNumber, pageSize);
    }

    @PostMapping(value = "/add", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ItemDTO addIngredient(@RequestPart("item") Item item,
                                 @RequestPart("image") MultipartFile image) {
        try {
            Image images = imageService.uploadFile(image);
            item.setImage(images);
            messagingTemplate.convertAndSend("/stockUpdate", item.getName());
            return itemService.addItem(item);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    @GetMapping("/{id}")
    public ItemDTO getItemById(@PathVariable("id") Long itemId) {
        return itemService.getItemById(itemId);
    }

    @PutMapping(value = "/update", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ItemDTO updateItem(@RequestPart("item") Item item,
                              @RequestPart("image") MultipartFile image) {
        try {
            Image images = imageService.uploadFile(image);
            messagingTemplate.convertAndSend("/stockUpdate", item.getId());

            return itemService.updateItem(item, images);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    @DeleteMapping("/delete/{id}")
    public void deleteItem(@PathVariable("id") Long itemId) {
        itemService.deleteItem(itemId);
    }

    @GetMapping("/category/{name}")
    public List<ItemDTO> getItemsByCategory_Name(@PathVariable("name") String name) {
        return itemService.getItemsByCategory_Name(name);
    }
}
