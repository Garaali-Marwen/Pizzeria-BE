package com.example.pizzeria.Services;

import com.example.pizzeria.Configuration.ItemAvailabilityRequest;
import com.example.pizzeria.Configuration.ItemAvailabilityResponse;
import com.example.pizzeria.DTOs.OrderItemDTO;
import com.example.pizzeria.Entities.OrderItem;

import java.util.List;

public interface OrderItemService {

    OrderItemDTO addOrderItem(OrderItem orderItem);

    List<OrderItemDTO> getAllOrderItems();

    OrderItemDTO getOrderItemById(Long orderItemId);

    OrderItemDTO updateOrderItem(OrderItemDTO orderItemDTO);

    void deleteOrderItem(Long orderItemId);

    List<OrderItemDTO> getOrderItemsByClientId(Long clientId);

    ItemAvailabilityResponse verifyItemAvailability(ItemAvailabilityRequest itemAvailabilityRequest);

}
