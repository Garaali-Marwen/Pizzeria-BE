package com.example.pizzeria.Repositories;

import com.example.pizzeria.Entities.Client;
import com.example.pizzeria.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    List<Client> findTop10ByOrderByIdDesc();

}
