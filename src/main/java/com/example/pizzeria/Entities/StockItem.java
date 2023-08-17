package com.example.pizzeria.Entities;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class StockItem {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @OneToOne
    private Item item;
    @OneToOne
    private Ingredient ingredient;
    private float quantity;
}
