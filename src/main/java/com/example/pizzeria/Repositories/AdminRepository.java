package com.example.pizzeria.Repositories;

import com.example.pizzeria.Entities.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin, Long> {
}
