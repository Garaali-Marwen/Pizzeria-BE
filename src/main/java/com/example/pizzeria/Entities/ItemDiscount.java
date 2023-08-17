package com.example.pizzeria.Entities;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class ItemDiscount {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private double discount;
    @ManyToOne
    private Item item;
    @ManyToOne
    private Offer offer;
}
