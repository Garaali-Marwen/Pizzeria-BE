package com.example.pizzeria.Services;

import com.example.pizzeria.DTOs.OrderDTO;
import com.example.pizzeria.Entities.Order;
import com.example.pizzeria.Enum.OrderType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public interface OrderService {

    OrderDTO addOrder(Order order);

    Page<OrderDTO> getAllOrders(Integer pageNumber, Integer pageSize);

    List<OrderDTO> getAllOrdersForMap();

    OrderDTO getOrderById(Long orderId);

    OrderDTO updateOrder(OrderDTO orderDTO);

    void deleteOrder(Long orderId);

    Page<OrderDTO> findOrdersOfTodayOrderByDateDesc(Integer pageNumber, Integer pageSize);

    List<Object[]> getOrdersForActualDay();

    List<Object[]> getOrdersForActualWeek();

    List<Object[]> getOrdersForActualMonth();

    int countByClient_Id(Long clientId);

    List<Map<OrderType, List<String[]>>> getOrdersByOrderTypeAndDate();

    Page<OrderDTO> getOrdersByClient_Id(Long clientId, Integer pageNumber, Integer pageSize);

    Page<OrderDTO> getOrdersOfTodayByClient_IdOrderByDateDesc(Long clientId, Integer pageNumber, Integer pageSize);

    Page<OrderDTO> getOrdersByClient_IdAndState_PendingOrderByDateDesc(Long clientId, Integer pageNumber, Integer pageSize);

    Page<OrderDTO> getOrdersByClient_IdAndState_ReadyOrderByDateDesc(Long clientId, Integer pageNumber, Integer pageSize);
}
