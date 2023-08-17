package com.example.pizzeria.Entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Entity
public class Offer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String description;
    private double totalPrice;
    private Date beginDate;
    private Date endDate;
    @OneToMany
    private List<ItemDiscount> itemsDiscount = new ArrayList<>();
    @OneToOne(cascade = CascadeType.ALL)
    private Image image;
}
