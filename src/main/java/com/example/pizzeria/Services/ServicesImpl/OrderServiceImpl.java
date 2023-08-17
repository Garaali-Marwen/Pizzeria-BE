package com.example.pizzeria.Services.ServicesImpl;

import com.example.pizzeria.Configuration.TwilioRequest;
import com.example.pizzeria.DTOs.Mappers.OrderDTOMapper;
import com.example.pizzeria.DTOs.OrderDTO;
import com.example.pizzeria.Entities.Address;
import com.example.pizzeria.Entities.Order;
import com.example.pizzeria.Entities.OrderItem;
import com.example.pizzeria.Entities.Transaction;
import com.example.pizzeria.Enum.OrderState;
import com.example.pizzeria.Repositories.OrderItemRepository;
import com.example.pizzeria.Repositories.OrderRepository;
import com.example.pizzeria.Services.AddressService;
import com.example.pizzeria.Services.OrderService;
import com.example.pizzeria.Services.StockItemService;
import com.example.pizzeria.Services.TransactionService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.NoSuchElementException;

@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderDTOMapper orderDTOMapper;
    private final OrderItemRepository orderItemRepository;
    private final StockItemService stockItemService;
    private final TransactionService transactionService;
    private final TwilioService twilioService;
    private final AddressService addressService;

    @Override
    public OrderDTO addOrder(Order order) {
        Address address = addressService.addAddress(order.getAddress());
        order.setAddress(address);
        order.setDate(new Date());
        order.setState(OrderState.PENDING);
        Order newOrder = orderRepository.save(order);
        for (OrderItem orderItem : orderItemRepository.findAllByClient_IdAndOrderIsNull(order.getClient().getId())) {
            orderItem.setOrder(newOrder);
            orderItemRepository.save(orderItem);
            stockItemService.updateStockItemByOrderItem(orderItem);
        }
        transactionService.addTransaction(Transaction
                .builder()
                .client(newOrder.getClient())
                .order(newOrder)
                .build());
        return orderDTOMapper.apply(newOrder);
    }

    @Override
    public Page<OrderDTO> getAllOrders(Integer pageNumber, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        return orderRepository.findAll(pageable).map(orderDTOMapper);
    }

    @Override
    public OrderDTO getOrderById(Long orderId) {
        return orderDTOMapper.apply(orderRepository.findById(orderId)
                .orElseThrow(() -> new NoSuchElementException("No order found with id: " + orderId)));
    }

    @Override
    public OrderDTO updateOrder(OrderDTO orderDTO) {
        Order order = orderRepository.findById(orderDTO.id())
                .orElseThrow(() -> new NoSuchElementException("No order found with id: " + orderDTO.id()));
        order.setDate(orderDTO.date());
        order.setComment(orderDTO.comment());
        order.setPrice(orderDTO.price());
        order.setState(orderDTO.state());
        order.setOrderType(orderDTO.orderType());
        orderRepository.save(order);
        if (order.isSmsNotification())
            twilioService.sendSMS(new TwilioRequest("+216" + order.getClient().getPhoneNumber(), createMessage(order)));
        return orderDTO;
    }

    @Override
    public void deleteOrder(Long orderId) {
        orderRepository.deleteById(orderId);
    }


    @Override
    public Page<OrderDTO> findOrdersOfTodayOrderByDateDesc(Integer pageNumber, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atTime(LocalTime.MIN);
        LocalDateTime endOfDay = today.atTime(LocalTime.MAX);

        Date startDate = java.sql.Timestamp.valueOf(startOfDay);
        Date endDate = java.sql.Timestamp.valueOf(endOfDay);
        return orderRepository.findOrdersOfTodayOrderByDateDesc(startDate, endDate, pageable).map(orderDTOMapper);
    }

    private int getOrderNumber(Long orderId) {
        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atTime(LocalTime.MIN);
        LocalDateTime endOfDay = today.atTime(LocalTime.MAX);

        Date startDate = java.sql.Timestamp.valueOf(startOfDay);
        Date endDate = java.sql.Timestamp.valueOf(endOfDay);
        return orderRepository.findOrderNumber(startDate, endDate, orderId);
    }

    private String createMessage(Order order) {
        StringBuilder orderItems = new StringBuilder();
        for (OrderItem orderItem : order.getOrderItems()) {
            orderItems.append("- ").append(orderItem.getQuantity()).append(" ").append(orderItem.getItem().getName()).append("\n");
            if (order.getOrderItems().indexOf(orderItem) != order.getOrderItems().size() - 1)
                orderItems.append(", ");
        }
        return "\"Bonjour " + order.getClient().getFirstName() + ",\n" +
                "\n" +
                "Votre commande N° " + getOrderNumber(order.getId()) +
                " est maintenant prête pour récupération. Voici les détails de votre commande : \n "
                + orderItems +
                " est prête. Veuillez vous rendre chez notre restaurant  pour la récupérer.\n" +
                "\n" +
                "Nous vous remercions pour votre confiance.";
    }
}
