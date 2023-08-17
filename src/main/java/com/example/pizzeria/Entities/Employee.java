package com.example.pizzeria.Entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import lombok.Data;

@Data
@Entity
public class Employee extends User {
    @OneToOne(cascade = CascadeType.ALL)
    private Image image;
}
