package com.example.pizzeria.Controllers;

import com.example.pizzeria.DTOs.OrderDTO;
import com.example.pizzeria.Entities.Order;
import com.example.pizzeria.Enum.OrderType;
import com.example.pizzeria.Services.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/order")
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class OrderController {

    private final OrderService orderService;
    private final SimpMessagingTemplate messagingTemplate;

    @GetMapping("/all/{pageNumber}/{pageSize}")
    public Page<OrderDTO> getAllOrders(@PathVariable("pageNumber") Integer pageNumber, @PathVariable("pageSize") Integer pageSize) {
        return orderService.getAllOrders(pageNumber, pageSize);
    }

    @GetMapping("/all")
    public List<OrderDTO> getAllOrdersForMap() {
        return orderService.getAllOrdersForMap();
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
        messagingTemplate.convertAndSend("/orderUpdate", orderDTO.id());
        return orderService.updateOrder(orderDTO);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteOrder(@PathVariable("id") Long orderId) {
        orderService.deleteOrder(orderId);
    }

    @GetMapping("/all/today/{pageNumber}/{pageSize}")
    public Page<OrderDTO> findOrdersOfTodayOrderByDateDesc(@PathVariable("pageNumber") Integer pageNumber, @PathVariable("pageSize") Integer pageSize) {
        return orderService.findOrdersOfTodayOrderByDateDesc(pageNumber, pageSize);
    }

    @GetMapping("/day")
    public List<Object[]> getOrdersForActualDay() {
        return orderService.getOrdersForActualDay();
    }

    @GetMapping("/week")
    public List<Object[]> getOrdersForActualWeek() {
        return orderService.getOrdersForActualWeek();
    }

    @GetMapping("/month")
    public List<Object[]> getOrdersForActualMonth() {
        return orderService.getOrdersForActualMonth();
    }

    @GetMapping("/client/{id}")
    public int countByClient_Id(@PathVariable("id") Long clientId) {
        return orderService.countByClient_Id(clientId);
    }

    @GetMapping("/type")
    public List<Map<OrderType, List<String[]>>> getOrdersByOrderTypeAndDate() {
        return orderService.getOrdersByOrderTypeAndDate();
    }

    @GetMapping("/client/orders/{id}/{pageNumber}/{pageSize}")
    public Page<OrderDTO> getOrdersByClient_Id(@PathVariable("id") Long clientId, @PathVariable("pageNumber") Integer pageNumber, @PathVariable("pageSize") Integer pageSize) {
        return orderService.getOrdersByClient_Id(clientId, pageNumber, pageSize);
    }

    @GetMapping("/client/orders/today/{id}/{pageNumber}/{pageSize}")
    public Page<OrderDTO> getOrdersOfTodayByClient_IdOrderByDateDesc(@PathVariable("id") Long clientId, @PathVariable("pageNumber") Integer pageNumber, @PathVariable("pageSize") Integer pageSize) {
        return orderService.getOrdersOfTodayByClient_IdOrderByDateDesc(clientId, pageNumber, pageSize);
    }

    @GetMapping("/client/orders/pending/{id}/{pageNumber}/{pageSize}")
    public Page<OrderDTO> getOrdersByClient_IdAndState_PendingOrderByDateDesc(@PathVariable("id") Long clientId, @PathVariable("pageNumber") Integer pageNumber, @PathVariable("pageSize") Integer pageSize) {
        return orderService.getOrdersByClient_IdAndState_PendingOrderByDateDesc(clientId, pageNumber, pageSize);
    }

    @GetMapping("/client/orders/ready/{id}/{pageNumber}/{pageSize}")
    public Page<OrderDTO> getOrdersByClient_IdAndState_ReadyOrderByDateDesc(@PathVariable("id") Long clientId, @PathVariable("pageNumber") Integer pageNumber, @PathVariable("pageSize") Integer pageSize) {
        return orderService.getOrdersByClient_IdAndState_ReadyOrderByDateDesc(clientId, pageNumber, pageSize);
    }

}
