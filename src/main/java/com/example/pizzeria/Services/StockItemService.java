package com.example.pizzeria.Services;

import com.example.pizzeria.DTOs.StockItemDTO;
import com.example.pizzeria.Entities.OrderItem;
import com.example.pizzeria.Entities.StockItem;
import org.springframework.data.domain.Page;

import java.util.List;

public interface StockItemService {

    StockItemDTO addStockItem(StockItem stockItem);
    Page<StockItemDTO> getAllStockItems(Integer pageNumber, Integer pageSize);
    StockItemDTO getStockItemById(Long stockItemId);
    StockItemDTO updateStockItem(StockItemDTO stockItemDTO);
    void deleteStockItem(Long stockItemId);
    void updateStockItemByOrderItem(OrderItem orderItem);
}
