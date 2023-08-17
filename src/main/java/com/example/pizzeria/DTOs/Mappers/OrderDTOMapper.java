package com.example.pizzeria.DTOs.Mappers;

import com.example.pizzeria.DTOs.OrderDTO;
import com.example.pizzeria.Entities.Order;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class OrderDTOMapper implements Function<Order, OrderDTO> {

    private final ClientDTOMapper clientDTOMapper;
    private final OrderItemDTOMapper orderItemDTOMapper;

    @Override
    public OrderDTO apply(Order order) {
        return new OrderDTO(
                order.getId(),
                order.getDate(),
                clientDTOMapper.apply(order.getClient()),
                order.getOrderItems().stream().map(orderItemDTOMapper).collect(Collectors.toList()),
                order.getOrderType(),
                order.getPrice(),
                order.getState(),
                order.getComment(),
                order.getAddress()
        );
    }
}
