package com.example.pizzeria.Repositories;

import com.example.pizzeria.Entities.ItemDiscount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemDiscountRepository extends JpaRepository<ItemDiscount, Long> {
}
