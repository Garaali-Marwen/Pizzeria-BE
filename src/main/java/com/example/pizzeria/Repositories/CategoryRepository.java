package com.example.pizzeria.Repositories;

import com.example.pizzeria.Entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    Category getCategoryByItems_Id(Long id);
}
