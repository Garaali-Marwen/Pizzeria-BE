package com.example.pizzeria.Entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "category")
    private List<Item> items = new ArrayList<>();
    @OneToOne(cascade = CascadeType.ALL)
    private Image image;
    @OneToOne(cascade = CascadeType.ALL)
    private Image icon;
}
