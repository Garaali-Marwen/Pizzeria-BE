package com.example.pizzeria.Entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class Client extends User {

    private int phoneNumber;
    @OneToOne(cascade = CascadeType.ALL)
    private Image image;
    @OneToMany(mappedBy = "client")
    private List<Order> orders = new ArrayList<>();
    @OneToMany(mappedBy = "client")
    private List<Transaction> transactions = new ArrayList<>();
}
