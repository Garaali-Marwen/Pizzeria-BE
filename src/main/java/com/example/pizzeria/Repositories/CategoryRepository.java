package com.example.pizzeria.Repositories;

import com.example.pizzeria.Configuration.CategoryIncomeResponse;
import com.example.pizzeria.Entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Category getCategoryByItems_Id(Long id);
    @Query("SELECT NEW com.example.pizzeria.Configuration.CategoryIncomeResponse(c.name, " +
            "EXTRACT(MONTH FROM o.date) , SUM(oi.price)) " +
            "FROM Category c " +
            "LEFT JOIN c.items i " +
            "LEFT JOIN i.orderItems oi " +
            "LEFT JOIN oi.order o " +
            "WHERE EXTRACT(YEAR FROM o.date) = :year " +
            "GROUP BY c.name, EXTRACT(MONTH FROM o.date) " +
            "ORDER BY c.name, EXTRACT(MONTH FROM o.date)")
    List<CategoryIncomeResponse> getCategoryIncomeForYear(@Param("year") int year);

    @Query("SELECT c.name, i.name, COUNT(oi.id) " +
            "FROM Category c " +
            "JOIN c.items i " +
            "LEFT JOIN i.orderItems oi " +
            "WHERE c.id = :categoryId " +
            "AND oi.order IS NOT NULL " +
            "GROUP BY c.name, i.name")
    List<Object[]> getOrdersCountByItemAndCategory(Long categoryId);
}
