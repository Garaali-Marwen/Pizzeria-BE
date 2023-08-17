package com.example.pizzeria.Controllers;

import com.example.pizzeria.DTOs.StockItemDTO;
import com.example.pizzeria.Entities.StockItem;
import com.example.pizzeria.Services.StockItemService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;


@RestController
@RequestMapping("/api/stockItem")
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class StockItemController {

    private final StockItemService stockItemService;
    private final SimpMessagingTemplate messagingTemplate;

    @GetMapping("/all/{pageNumber}/{pageSize}")
    public Page<StockItemDTO> getAllStockItems(@PathVariable("pageNumber") Integer pageNumber, @PathVariable("pageSize") Integer pageSize){
        return stockItemService.getAllStockItems(pageNumber, pageSize);
    }
    @PostMapping("/add")
    public StockItemDTO addStockItem(@RequestBody StockItem stockItem){
        messagingTemplate.convertAndSend("/stockUpdate", stockItem.getItem().getId());
        return stockItemService.addStockItem(stockItem);
    }
    @GetMapping("/{id}")
    public StockItemDTO getStockItemById(@PathVariable("id") Long stockItemId){
        return stockItemService.getStockItemById(stockItemId);
    }
    @PutMapping("/update")
    public StockItemDTO updateStockItem(@RequestBody StockItemDTO stockItemDTO){
        messagingTemplate.convertAndSend("/stockUpdate", stockItemDTO.id());
        return stockItemService.updateStockItem(stockItemDTO);
    }
    @DeleteMapping("/delete/{id}")
    public void deleteStockItem(@PathVariable("id") Long stockItemId){
        stockItemService.deleteStockItem(stockItemId);
    }
}
