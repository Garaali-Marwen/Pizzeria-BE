package com.example.pizzeria.Repositories;

import com.example.pizzeria.Entities.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("SELECT o FROM Order o WHERE o.date BETWEEN :startDate AND :endDate ORDER BY o.date DESC")
    Page<Order> findOrdersOfTodayOrderByDateDesc(@Param("startDate") Date startDate, @Param("endDate") Date endDate, Pageable pageable);

    @Query("SELECT COUNT(o) FROM Order o WHERE o.date BETWEEN :startDate AND :endDate AND o.id <= :orderId")
    int findOrderNumber(@Param("startDate") Date startDate, @Param("endDate") Date endDate, @Param("orderId") Long orderId);
}
