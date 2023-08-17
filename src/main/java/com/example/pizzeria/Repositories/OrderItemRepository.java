package com.example.pizzeria.Repositories;

import com.example.pizzeria.Entities.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    List<OrderItem> findAllByClient_IdAndOrderIsNull(Long clientId);
}
