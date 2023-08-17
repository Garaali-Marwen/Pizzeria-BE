package com.example.pizzeria.Repositories;

import com.example.pizzeria.Entities.StockItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StockItemRepository extends JpaRepository<StockItem, Long> {
    StockItem getStockItemByItem_Id(Long id);
    StockItem getStockItemByIngredient_Id(Long id);
}
