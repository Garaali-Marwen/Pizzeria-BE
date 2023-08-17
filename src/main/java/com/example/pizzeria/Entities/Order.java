package com.example.pizzeria.Entities;

import com.example.pizzeria.Enum.OrderState;
import com.example.pizzeria.Enum.OrderType;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Date date;
    @ManyToOne
    private Client client;
    @OneToMany(mappedBy = "order")
    private List<OrderItem> orderItems = new ArrayList<>();
    @Enumerated(EnumType.STRING)
    private OrderType orderType;
    private double price;
    @Enumerated(EnumType.STRING)
    private OrderState state;
    @Column(columnDefinition = "TEXT", length = 5000)
    private String comment;
    private boolean smsNotification;
    @OneToOne
    private Address address;
}
