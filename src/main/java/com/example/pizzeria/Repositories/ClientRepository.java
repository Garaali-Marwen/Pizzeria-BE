package com.example.pizzeria.Repositories;

import com.example.pizzeria.Entities.Client;
import com.example.pizzeria.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
}
