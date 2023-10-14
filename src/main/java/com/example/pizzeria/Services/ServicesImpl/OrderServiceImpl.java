package com.example.pizzeria.Services.ServicesImpl;

import com.example.pizzeria.Configuration.TwilioRequest;
import com.example.pizzeria.DTOs.Mappers.OrderDTOMapper;
import com.example.pizzeria.DTOs.OrderDTO;
import com.example.pizzeria.Entities.Address;
import com.example.pizzeria.Entities.Order;
import com.example.pizzeria.Entities.OrderItem;
import com.example.pizzeria.Entities.Transaction;
import com.example.pizzeria.Enum.OrderState;
import com.example.pizzeria.Enum.OrderType;
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
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;

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
    public List<OrderDTO> getAllOrdersForMap() {
        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atTime(LocalTime.MIN);
        LocalDateTime endOfDay = today.atTime(LocalTime.MAX);
        Date startDate = java.sql.Timestamp.valueOf(startOfDay);
        Date endDate = java.sql.Timestamp.valueOf(endOfDay);
        return orderRepository.findOrdersOfTodayOrderByDateDescAndState(startDate, endDate).stream().map(orderDTOMapper).collect(Collectors.toList());
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

    @Override
    public List<Object[]> getOrdersForActualDay() {
        List<Object[]> result = orderRepository.getOrdersPerDay();
        List<Object[]> response = new ArrayList<>();
        result.sort(Comparator.comparingInt(row -> (int) row[3]));

        for (Object[] row : result) {
            int year = ((Number) row[0]).intValue();
            int month = ((Number) row[1]).intValue();
            int day = ((Number) row[2]).intValue();
            int hour = ((Number) row[3]).intValue();
            long orderCount = (long) row[4];

            LocalDateTime date = LocalDateTime.of(year, month, day, hour, 0);

            response.add(new Object[]{date, orderCount});
        }

        return response;
    }

    @Override
    public List<Object[]> getOrdersForActualWeek() {
        List<Object[]> result = orderRepository.getOrdersPerWeek();
        List<Object[]> response = new ArrayList<>();
        result.sort(Comparator.comparing(row -> ((Number) row[2]).intValue()));

        for (Object[] row : result) {
            int year = ((Number) row[0]).intValue();
            int month = ((Number) row[1]).intValue();
            int day = ((Number) row[2]).intValue();
            long orderCount = (long) row[3];

            LocalDate date = LocalDate.of(year, month, day);

            response.add(new Object[]{date, orderCount});
        }
        return response;
    }

    @Override
    public List<Object[]> getOrdersForActualMonth() {
        List<Object[]> result = orderRepository.getOrdersPerMonth();
        List<Object[]> response = new ArrayList<>();
        result.sort(Comparator.comparing(row -> ((Number) row[2]).intValue()));

        for (Object[] row : result) {
            int year = ((Number) row[0]).intValue();
            int month = ((Number) row[1]).intValue();
            int day = ((Number) row[2]).intValue();
            long orderCount = (long) row[3];

            LocalDate date = LocalDate.of(year, month, day);

            response.add(new Object[]{date, orderCount});
        }
        return response;
    }

    @Override
    public int countByClient_Id(Long clientId) {
        return orderRepository.countByClient_Id(clientId);
    }

    @Override
    public List<Map<OrderType, List<String[]>>> getOrdersByOrderTypeAndDate() {
        List<Object[]> result = orderRepository.getOrdersByOrderTypeAndDate();
        List<Map<OrderType, List<String[]>>> data = new ArrayList<>();
        Map<OrderType, List<String[]>> delivery = new HashMap<>();
        delivery.put(OrderType.DELIVERY, new ArrayList<>());

        Map<OrderType, List<String[]>> dine_in = new HashMap<>();
        dine_in.put(OrderType.DINE_IN, new ArrayList<>());

        Map<OrderType, List<String[]>> takeaway = new HashMap<>();
        takeaway.put(OrderType.TAKEAWAY, new ArrayList<>());

        for (Object[] row : result) {
            OrderType type = (OrderType) row[0];
            int day = ((Number) row[1]).intValue();
            int month = ((Number) row[2]).intValue();
            int year = ((Number) row[3]).intValue();
            int number = ((Number) row[4]).intValue();
            switch (type) {
                case DELIVERY: {
                    delivery.get(OrderType.DELIVERY).add(new String[]{String.valueOf(LocalDate.of(year, month, day)), String.valueOf(number)});
                }
                break;
                case TAKEAWAY: {
                    takeaway.get(OrderType.TAKEAWAY).add(new String[]{String.valueOf(LocalDate.of(year, month, day)), String.valueOf(number)});
                }
                break;
                case DINE_IN: {
                    dine_in.get(OrderType.DINE_IN).add(new String[]{String.valueOf(LocalDate.of(year, month, day)), String.valueOf(number)});
                }
                break;
            }
        }
        for (List<String[]> list : delivery.values()) {
            list.sort(Comparator.comparing(arr -> arr[0]));
        }
        for (List<String[]> list : takeaway.values()) {
            list.sort(Comparator.comparing(arr -> arr[0]));
        }
        for (List<String[]> list : dine_in.values()) {
            list.sort(Comparator.comparing(arr -> arr[0]));
        }
        data.add(delivery);
        data.add(takeaway);
        data.add(dine_in);
        return data;
    }

    @Override
    public Page<OrderDTO> getOrdersByClient_Id(Long clientId, Integer pageNumber, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        return orderRepository.getOrdersByClient_IdOrderByDateDesc(clientId, pageable).map(orderDTOMapper);
    }

    @Override
    public Page<OrderDTO> getOrdersOfTodayByClient_IdOrderByDateDesc(Long clientId, Integer pageNumber, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atTime(LocalTime.MIN);
        LocalDateTime endOfDay = today.atTime(LocalTime.MAX);

        Date startDate = java.sql.Timestamp.valueOf(startOfDay);
        Date endDate = java.sql.Timestamp.valueOf(endOfDay);
        return orderRepository.getOrdersOfTodayByClient_IdOrderByDateDesc(clientId, pageable, startDate, endDate).map(orderDTOMapper);
    }

    @Override
    public Page<OrderDTO> getOrdersByClient_IdAndState_PendingOrderByDateDesc(Long clientId, Integer pageNumber, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return orderRepository.getOrdersByClient_IdAndStateOrderByDateDesc(clientId,OrderState.PENDING, pageable).map(orderDTOMapper);
    }

    @Override
    public Page<OrderDTO> getOrdersByClient_IdAndState_ReadyOrderByDateDesc(Long clientId, Integer pageNumber, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return orderRepository.getOrdersByClient_IdAndStateOrderByDateDesc(clientId,OrderState.READY, pageable).map(orderDTOMapper);
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
                "Veuillez vous rendre chez notre restaurant  pour la récupérer.\n" +
                "\n" +
                "Nous vous remercions pour votre confiance.";
    }
}
