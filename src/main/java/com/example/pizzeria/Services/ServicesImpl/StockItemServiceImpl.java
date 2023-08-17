package com.example.pizzeria.Services.ServicesImpl;

import com.example.pizzeria.DTOs.Mappers.StockItemDTOMapper;
import com.example.pizzeria.DTOs.StockItemDTO;
import com.example.pizzeria.Entities.*;
import com.example.pizzeria.Enum.IngredientType;
import com.example.pizzeria.Repositories.StockItemRepository;
import com.example.pizzeria.Services.StockItemService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@AllArgsConstructor
public class StockItemServiceImpl implements StockItemService {

    private final StockItemRepository stockItemRepository;
    private final StockItemDTOMapper stockItemDTOMapper;


    @Override
    public StockItemDTO addStockItem(StockItem stockItem) {
        if (stockItem.getItem() != null) {
            StockItem stockItem1 = stockItemRepository.getStockItemByItem_Id(stockItem.getItem().getId());
            if (stockItem1 != null) {
                stockItem1.setQuantity(stockItem1.getQuantity() + stockItem.getQuantity());
                return stockItemDTOMapper.apply(stockItemRepository.save(stockItem1));
            }
        } else if (stockItem.getIngredient() != null) {
            StockItem stockItem1 = stockItemRepository.getStockItemByIngredient_Id(stockItem.getIngredient().getId());
            if (stockItem1 != null) {
                stockItem1.setQuantity(stockItem1.getQuantity() + stockItem.getQuantity());
                return stockItemDTOMapper.apply(stockItemRepository.save(stockItem1));
            }
        }
        return stockItemDTOMapper.apply(stockItemRepository.save(stockItem));
    }

    @Override
    public Page<StockItemDTO> getAllStockItems(Integer pageNumber, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return stockItemRepository.findAll(pageable).map(stockItemDTOMapper);
    }

    @Override
    public StockItemDTO getStockItemById(Long stockItemId) {
        return stockItemDTOMapper.apply(stockItemRepository.findById(stockItemId)
                .orElseThrow(() -> new NoSuchElementException("No stockItem found with id: " + stockItemId)));
    }

    @Override
    public StockItemDTO updateStockItem(StockItemDTO stockItemDTO) {
        StockItem stockItem = stockItemRepository.findById(stockItemDTO.id())
                .orElseThrow(() -> new NoSuchElementException("No stockItem found with id: " + stockItemDTO.id()));
        stockItem.setQuantity(stockItemDTO.quantity());
        stockItemRepository.save(stockItem);
        return stockItemDTO;
    }

    @Override
    public void deleteStockItem(Long stockItemId) {
        stockItemRepository.deleteById(stockItemId);
    }

    @Override
    public void updateStockItemByOrderItem(OrderItem orderItem) {
        StockItem stockItem = new StockItem();
        if (orderItem.getItem().getItemIngredients().isEmpty()) {
            stockItem = stockItemRepository.getStockItemByItem_Id(orderItem.getItem().getId());
            stockItem.setQuantity(stockItem.getQuantity() - orderItem.getQuantity());
        } else {
            for (ItemIngredient itemIngredient : orderItem.getItem().getItemIngredients()) {
                stockItem = stockItemRepository.getStockItemByIngredient_Id(itemIngredient.getIngredient().getId());
                if (itemIngredient.getType().equals(IngredientType.PRIMARY)
                        ||
                        orderItem.getIngredients().stream().anyMatch(ingredient -> ingredient.getId() == itemIngredient.getIngredient().getId()))
                    stockItem.setQuantity(stockItem.getQuantity() - (itemIngredient.getQuantity() * orderItem.getQuantity()));
            }
        }
        stockItemRepository.save(stockItem);

    }

}
