package com.example.pizzeria.Controllers;

import com.example.pizzeria.DTOs.OrderDTO;
import com.example.pizzeria.Entities.Order;
import com.example.pizzeria.Services.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order")
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/all/{pageNumber}/{pageSize}")
    public Page<OrderDTO> getAllOrders(@PathVariable("pageNumber") Integer pageNumber,@PathVariable("pageSize") Integer pageSize) {
        return orderService.getAllOrders(pageNumber, pageSize);
    }

    @PostMapping("/add")
    public OrderDTO addOrder(@RequestBody Order order) {
        return orderService.addOrder(order);
    }

    @MessageMapping("/new")
    @SendTo({"/newOrder", "/stockUpdate"})
    public Long newOrder(@Payload Long orderId) {
        return orderId;
    }

    @GetMapping("/{id}")
    public OrderDTO getOrderById(@PathVariable("id") Long orderId) {
        return orderService.getOrderById(orderId);
    }

    @PutMapping("/update")
    public OrderDTO updateOrder(@RequestBody OrderDTO orderDTO) {
        return orderService.updateOrder(orderDTO);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteOrder(@PathVariable("id") Long orderId) {
        orderService.deleteOrder(orderId);
    }

    @GetMapping("/all/today/{pageNumber}/{pageSize}")
    public Page<OrderDTO> findOrdersOfTodayOrderByDateDesc(@PathVariable("pageNumber") Integer pageNumber,@PathVariable("pageSize") Integer pageSize) {
        return orderService.findOrdersOfTodayOrderByDateDesc(pageNumber, pageSize);
    }
}
