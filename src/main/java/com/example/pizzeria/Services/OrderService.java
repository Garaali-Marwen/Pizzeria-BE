package com.example.pizzeria.Services;

import com.example.pizzeria.DTOs.OrderDTO;
import com.example.pizzeria.Entities.Order;
import org.springframework.data.domain.Page;

import java.util.Date;
import java.util.List;

public interface OrderService {

    OrderDTO addOrder(Order order);
    Page<OrderDTO> getAllOrders(Integer pageNumber, Integer pageSize);
    OrderDTO getOrderById(Long orderId);
    OrderDTO updateOrder(OrderDTO orderDTO);
    void deleteOrder(Long orderId);
    Page<OrderDTO> findOrdersOfTodayOrderByDateDesc(Integer pageNumber, Integer pageSize);

}
