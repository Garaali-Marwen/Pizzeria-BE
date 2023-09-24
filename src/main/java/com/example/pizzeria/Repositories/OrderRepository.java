package com.example.pizzeria.Repositories;

import com.example.pizzeria.Entities.Order;
import com.example.pizzeria.Enum.OrderState;
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

    @Query("SELECT o FROM Order o WHERE o.date BETWEEN :startDate AND :endDate AND o.state = com.example.pizzeria.Enum.OrderState.PENDING ORDER BY o.date DESC")
    List<Order> findOrdersOfTodayOrderByDateDescAndState(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    @Query("SELECT EXTRACT(YEAR FROM o.date) as year, EXTRACT(MONTH FROM o.date) as month, EXTRACT(DAY FROM o.date) as day, COUNT(o) as orderCount " +
            "FROM Order o WHERE EXTRACT(YEAR FROM o.date) = EXTRACT(YEAR FROM CURRENT_DATE) AND EXTRACT(MONTH FROM o.date) = EXTRACT(MONTH FROM CURRENT_DATE) " +
            "GROUP BY year, month, day")
    List<Object[]> getOrdersPerMonth();


    @Query("SELECT EXTRACT(YEAR FROM o.date) as year, EXTRACT(MONTH FROM o.date) as month, EXTRACT(DAY FROM o.date) as day, COUNT(o) as orderCount FROM Order o " +
            "WHERE WEEK(o.date) = WEEK(CURRENT_DATE) AND YEAR(o.date) = YEAR(CURRENT_DATE) " +
            "GROUP BY year, month, day")
    List<Object[]> getOrdersPerWeek();

    @Query("SELECT EXTRACT(YEAR FROM o.date) as year, EXTRACT(MONTH FROM o.date) as month, EXTRACT(DAY FROM o.date) as day, HOUR(o.date) as hour, COUNT(o) as orderCount FROM Order o " +
            "WHERE DATE(o.date) = CURRENT_DATE " +
            "GROUP BY year, month, day, hour")
    List<Object[]> getOrdersPerDay();

    int countByClient_Id(Long clientId);

    @Query("SELECT o.orderType, EXTRACT(DAY FROM o.date) as day, EXTRACT(MONTH FROM o.date) as month, EXTRACT(YEAR FROM o.date) as year, COUNT(o) " +
            "FROM Order o " +
            "GROUP BY o.orderType, day, month, year")
    List<Object[]> getOrdersByOrderTypeAndDate();

    Page<Order> getOrdersByClient_IdOrderByDateDesc(Long clientId, Pageable pageable);

    @Query("SELECT o FROM Order o WHERE o.date BETWEEN :startDate AND :endDate AND o.client.id = :clientId ORDER BY o.date DESC")
    Page<Order> getOrdersOfTodayByClient_IdOrderByDateDesc(@Param("clientId") Long clientId, Pageable pageable, @Param("startDate") Date startDate, @Param("endDate") Date endDate);

    Page<Order> getOrdersByClient_IdAndStateOrderByDateDesc(Long clientId, OrderState state, Pageable pageable);


}
