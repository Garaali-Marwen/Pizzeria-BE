package com.example.pizzeria.Controllers;

import com.example.pizzeria.Configuration.ItemAvailabilityRequest;
import com.example.pizzeria.Configuration.ItemAvailabilityResponse;
import com.example.pizzeria.DTOs.OrderItemDTO;
import com.example.pizzeria.Entities.OrderItem;
import com.example.pizzeria.Services.OrderItemService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orderItem")
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class OrderItemController {

    private final OrderItemService orderItemService;

    @GetMapping("/all")
    public List<OrderItemDTO> getAllOrderItems(){
        return orderItemService.getAllOrderItems();
    }
    @PostMapping("/add")
    public OrderItemDTO addOrderItem(@RequestBody OrderItem orderItem){
        return orderItemService.addOrderItem(orderItem);
    }
    @GetMapping("/{id}")
    public OrderItemDTO getOrderItemById(@PathVariable("id") Long orderItemId){
        return orderItemService.getOrderItemById(orderItemId);
    }
    @PutMapping("/update")
    public OrderItemDTO updateOrderItem(@RequestBody OrderItemDTO orderItemDTO){
        return orderItemService.updateOrderItem(orderItemDTO);
    }
    @DeleteMapping("/delete/{id}")
    public void deleteOrderItem(@PathVariable("id") Long orderItemId){
        orderItemService.deleteOrderItem(orderItemId);
    }
    @GetMapping("/client/{id}")
    public List<OrderItemDTO> getOrderItemsByClientId(@PathVariable("id") Long clientId){
        return orderItemService.getOrderItemsByClientId(clientId);
    }

    @PostMapping("/verify-availability")
    public ItemAvailabilityResponse verifyItemAvailability(@RequestBody ItemAvailabilityRequest itemAvailabilityRequest){
        return orderItemService.verifyItemAvailability(itemAvailabilityRequest);
    }

}
