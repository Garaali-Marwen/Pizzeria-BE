package com.example.pizzeria.Services.ServicesImpl;

import com.example.pizzeria.Configuration.ItemAvailabilityRequest;
import com.example.pizzeria.Configuration.ItemAvailabilityResponse;
import com.example.pizzeria.DTOs.IngredientDTO;
import com.example.pizzeria.DTOs.ItemIngredientDTO;
import com.example.pizzeria.DTOs.Mappers.OrderItemDTOMapper;
import com.example.pizzeria.DTOs.OrderItemDTO;
import com.example.pizzeria.Entities.*;
import com.example.pizzeria.Enum.IngredientType;
import com.example.pizzeria.Repositories.*;
import com.example.pizzeria.Services.OrderItemService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class OrderItemServiceImpl implements OrderItemService {

    private final OrderItemRepository orderItemRepository;
    private final OrderItemDTOMapper orderItemDTOMapper;
    private final IngredientRepository ingredientRepository;
    private final ItemIngredientRepository itemIngredientRepository;
    private final StockItemRepository stockItemRepository;
    private final ItemRepository itemRepository;

    @Override
    public OrderItemDTO addOrderItem(OrderItem orderItem) {
        return orderItemDTOMapper.apply(orderItemRepository.save(orderItem));
    }

    @Override
    public List<OrderItemDTO> getAllOrderItems() {
        return orderItemRepository.findAll().stream().map(orderItemDTOMapper).collect(Collectors.toList());
    }

    @Override
    public OrderItemDTO getOrderItemById(Long orderItemId) {
        return orderItemDTOMapper.apply(orderItemRepository.findById(orderItemId)
                .orElseThrow(() -> new NoSuchElementException("No orderItem found with id: " + orderItemId)));
    }

    @Override
    public OrderItemDTO updateOrderItem(OrderItemDTO orderItemDTO) {
        OrderItem orderItem = orderItemRepository.findById(orderItemDTO.id())
                .orElseThrow(() -> new NoSuchElementException("No orderItem found with id: " + orderItemDTO.id()));
        double totalPrice = getTotalPrice(orderItemDTO, orderItem);

        List<Long> newIngredientIds = orderItemDTO.ingredients().stream()
                .map(IngredientDTO::id)
                .collect(Collectors.toList());

        List<Ingredient> newIngredients = ingredientRepository.findAllById(newIngredientIds);

        // Remove ingredients that are not in the new list
        orderItem.getIngredients().removeIf(existingIngredient -> !newIngredients.contains(existingIngredient));

        // Add new ingredients that are not already in the list
        for (Ingredient newIngredient : newIngredients) {
            if (!orderItem.getIngredients().contains(newIngredient)) {
                orderItem.getIngredients().add(newIngredient);
            }
        }

        orderItem.setSize(orderItemDTO.size());
        orderItem.setQuantity(orderItemDTO.quantity());
        orderItem.setPrice(totalPrice * orderItemDTO.quantity());

        OrderItem updatedOrderItem = orderItemRepository.save(orderItem);
        return orderItemDTOMapper.apply(updatedOrderItem);
    }


    private static double getTotalPrice(OrderItemDTO orderItemDTO, OrderItem orderItem) {
        double totalPrice = orderItemDTO.item().price();
        if (!orderItem.getItem().getSizes().isEmpty()) {
            for (Size size : orderItemDTO.item().sizes())
                if (size.getSize().equals(orderItemDTO.size()))
                    totalPrice = size.getPrice();
        }

        for (IngredientDTO ingredient : orderItemDTO.ingredients())
            for (ItemIngredientDTO itemIngredientDTO : orderItemDTO.item().itemIngredients())
                if (Objects.equals(itemIngredientDTO.ingredient().id(), ingredient.id()))
                    totalPrice += itemIngredientDTO.price();
        return totalPrice;
    }

    @Override
    public void deleteOrderItem(Long orderItemId) {
        orderItemRepository.deleteById(orderItemId);
    }

    @Override
    public List<OrderItemDTO> getOrderItemsByClientId(Long clientId) {
        return orderItemRepository.findAllByClient_IdAndOrderIsNull(clientId).stream().map(orderItemDTOMapper).collect(Collectors.toList());
    }

    @Override
    public ItemAvailabilityResponse verifyItemAvailability(ItemAvailabilityRequest itemAvailabilityRequest) {
        ItemAvailabilityResponse itemAvailabilityResponse;
        float prevQuantity;
        do {
            prevQuantity = itemAvailabilityRequest.orderItem.getQuantity();
            if (itemAvailabilityRequest.orderItems.isEmpty()) {
                itemAvailabilityResponse = verifyItemAvailabilityWithEmptyCart(itemAvailabilityRequest);
            } else {
                itemAvailabilityResponse = verifyItemAvailabilityWithNoEmptyCart(itemAvailabilityRequest);
            }
            itemAvailabilityRequest.orderItem.setQuantity((int) itemAvailabilityResponse.quantity);
        } while ((itemAvailabilityRequest.orderItem.getQuantity() != prevQuantity) && itemAvailabilityRequest.orderItem.getQuantity() != 0);

        return itemAvailabilityResponse;
    }




    private ItemAvailabilityResponse verifyItemAvailabilityWithEmptyCart(ItemAvailabilityRequest itemAvailabilityRequest) {
        Item item = itemRepository.findById(itemAvailabilityRequest.orderItem.getItem().getId()).orElseThrow(() -> new NoSuchElementException("Not found"));
        if (item != null)
            if (item.getItemIngredients().isEmpty()) {
                if (item.getStockItem().getQuantity() < itemAvailabilityRequest.orderItem.getQuantity()) {
                    if (itemAvailabilityRequest.orderItem.getQuantity() - 1 == 0)
                        return new ItemAvailabilityResponse(0, new ArrayList<>(), itemAvailabilityRequest.orderItem.getQuantity(), true);
                    return new ItemAvailabilityResponse(itemAvailabilityRequest.orderItem.getQuantity() - 1, new ArrayList<>(), itemAvailabilityRequest.orderItem.getQuantity(), false);
                }
            } else {
                List<Long> unavailableIngredients = new ArrayList<>();
                List<Long> unavailablePrimaryIngredients = new ArrayList<>();
                for (ItemIngredient itemIngredient : itemAvailabilityRequest.orderItem.getItem().getItemIngredients()) {
                    float quantityRequested = itemIngredient.getQuantity();
                    ItemIngredient ingredient;
                    //get ItemIngredient
                    if (itemIngredient.getType().equals(IngredientType.PRIMARY)) {
                        ingredient = itemIngredientRepository.getItemIngredientByIngredient_IdAndItem_IdAndType(itemIngredient.getIngredient().getId(), itemAvailabilityRequest.orderItem.getItem().getId(), IngredientType.SECONDARY);
                        if (itemIngredient.getType().equals(IngredientType.PRIMARY)
                                && stockItemRepository.getStockItemByIngredient_Id(itemIngredient.getIngredient().getId()).getQuantity()
                                < itemIngredient.getQuantity()
                                * itemAvailabilityRequest.orderItem.getQuantity())
                            unavailablePrimaryIngredients.add(itemIngredient.getIngredient().getId());
                    } else {
                        ingredient = itemIngredientRepository.getItemIngredientByIngredient_IdAndItem_IdAndType(itemIngredient.getIngredient().getId(), itemAvailabilityRequest.orderItem.getItem().getId(), IngredientType.PRIMARY);
                    }

                    if (ingredient != null) {
                        quantityRequested += ingredient.getQuantity();
                    }
                    if (stockItemRepository.getStockItemByIngredient_Id(itemIngredient.getIngredient().getId()).getQuantity() < quantityRequested * itemAvailabilityRequest.orderItem.getQuantity()) {
                        if (!unavailableIngredients.contains(itemIngredient.getIngredient().getId()))
                            unavailableIngredients.add(itemIngredient.getIngredient().getId());
                    }
                }
                if (!unavailablePrimaryIngredients.isEmpty())
                    if (itemAvailabilityRequest.orderItem.getQuantity() - 1 == 0) {
                        return new ItemAvailabilityResponse(0, unavailablePrimaryIngredients, itemAvailabilityRequest.orderItem.getQuantity(), true);
                    } else
                        return new ItemAvailabilityResponse(itemAvailabilityRequest.orderItem.getQuantity() - 1, itemAvailabilityRequest.ingredients, itemAvailabilityRequest.orderItem.getQuantity(), false);


                if (!unavailableIngredients.isEmpty())
                    if (itemAvailabilityRequest.orderItem.getIngredients().stream().map(Ingredient::getId).anyMatch(unavailableIngredients::contains))
                        if (itemAvailabilityRequest.orderItem.getQuantity() - 1 == 0)
                            return new ItemAvailabilityResponse(0, unavailableIngredients, itemAvailabilityRequest.orderItem.getQuantity(), true);
                        else
                            return new ItemAvailabilityResponse(itemAvailabilityRequest.orderItem.getQuantity() - 1, itemAvailabilityRequest.ingredients, itemAvailabilityRequest.orderItem.getQuantity(), false);

                    else
                        return new ItemAvailabilityResponse(itemAvailabilityRequest.orderItem.getQuantity(), unavailableIngredients, itemAvailabilityRequest.orderItem.getQuantity(), false);

            }
        return new ItemAvailabilityResponse(itemAvailabilityRequest.orderItem.getQuantity(), new ArrayList<>(), itemAvailabilityRequest.orderItem.getQuantity(), false);
    }


    private ItemAvailabilityResponse verifyItemAvailabilityWithNoEmptyCart(ItemAvailabilityRequest itemAvailabilityRequest) {
        Item item = itemRepository.findById(itemAvailabilityRequest.orderItem.getItem().getId()).orElseThrow(() -> new NoSuchElementException("Not found"));
        if (item != null)
            if (item.getItemIngredients().isEmpty()) {
                OrderItem orderItem = itemAvailabilityRequest.orderItems.stream()
                        .filter(orderItem1 -> orderItem1.getItem().getId() == item.getId())
                        .findFirst()
                        .orElse(null);
                float totalQuantity = itemAvailabilityRequest.orderItem.getQuantity();
                if (orderItem != null)
                    totalQuantity += orderItem.getQuantity();
                if (item.getStockItem().getQuantity() < totalQuantity) {
                    if (itemAvailabilityRequest.orderItem.getQuantity() - 1 == 0)
                        return new ItemAvailabilityResponse(0, new ArrayList<>(), itemAvailabilityRequest.orderItem.getQuantity(), true);
                    return new ItemAvailabilityResponse(itemAvailabilityRequest.orderItem.getQuantity() - 1, new ArrayList<>(), itemAvailabilityRequest.orderItem.getQuantity(), false);
                }
            } else {
                List<Long> unavailableIngredients = new ArrayList<>();
                List<Long> unavailablePrimaryIngredients = new ArrayList<>();
                for (ItemIngredient itemIngredient : itemAvailabilityRequest.orderItem.getItem().getItemIngredients()) {
                    float quantityRequested = itemIngredient.getQuantity();
                    ItemIngredient ingredient;
                    //get ItemIngredient
                    if (itemIngredient.getType().equals(IngredientType.PRIMARY)) {
                        {
                            ingredient = itemIngredientRepository.getItemIngredientByIngredient_IdAndItem_IdAndType(
                                    itemIngredient.getIngredient().getId(),
                                    itemAvailabilityRequest.orderItem.getItem().getId(),
                                    IngredientType.SECONDARY);
                            if (stockItemRepository.getStockItemByIngredient_Id(itemIngredient.getIngredient().getId()).getQuantity()
                                    < (itemIngredient.getQuantity()
                                    * itemAvailabilityRequest.orderItem.getQuantity())
                                    + ingredientQuantityUsedInCart(itemIngredient.getIngredient(), itemAvailabilityRequest.orderItems)) {
                                unavailablePrimaryIngredients.add(itemIngredient.getIngredient().getId());
                            }
                        }
                    } else {
                        ingredient = itemIngredientRepository.getItemIngredientByIngredient_IdAndItem_IdAndType(
                                itemIngredient.getIngredient().getId(),
                                itemAvailabilityRequest.orderItem.getItem().getId(),
                                IngredientType.PRIMARY);
                    }
                    if (ingredient != null) {
                        quantityRequested += ingredient.getQuantity();
                    }
                    if (stockItemRepository.getStockItemByIngredient_Id(itemIngredient.getIngredient().getId()).getQuantity()
                            < (quantityRequested * itemAvailabilityRequest.orderItem.getQuantity()) + ingredientQuantityUsedInCart(itemIngredient.getIngredient(), itemAvailabilityRequest.orderItems)) {
                        if (!unavailableIngredients.contains(itemIngredient.getIngredient().getId()))
                            unavailableIngredients.add(itemIngredient.getIngredient().getId());
                    }
                }
                if (!unavailablePrimaryIngredients.isEmpty()) {
                    if (itemAvailabilityRequest.orderItem.getQuantity() - 1 == 0) {
                        return new ItemAvailabilityResponse(0, unavailablePrimaryIngredients, itemAvailabilityRequest.orderItem.getQuantity(), true);
                    } else
                        return new ItemAvailabilityResponse(itemAvailabilityRequest.orderItem.getQuantity() - 1, itemAvailabilityRequest.ingredients, itemAvailabilityRequest.orderItem.getQuantity(), false);
                }

                if (!unavailableIngredients.isEmpty())
                    if (itemAvailabilityRequest.orderItem.getIngredients().stream().map(Ingredient::getId).anyMatch(unavailableIngredients::contains))
                        if (itemAvailabilityRequest.orderItem.getQuantity() - 1 == 0)
                            return new ItemAvailabilityResponse(0, unavailableIngredients, itemAvailabilityRequest.orderItem.getQuantity(), true);
                        else
                            return new ItemAvailabilityResponse(itemAvailabilityRequest.orderItem.getQuantity() - 1, itemAvailabilityRequest.ingredients, itemAvailabilityRequest.orderItem.getQuantity(), false);

                    else
                        return new ItemAvailabilityResponse(itemAvailabilityRequest.orderItem.getQuantity(), unavailableIngredients, itemAvailabilityRequest.orderItem.getQuantity(), false);

            }
        return new ItemAvailabilityResponse(itemAvailabilityRequest.orderItem.getQuantity(), new ArrayList<>(), itemAvailabilityRequest.orderItem.getQuantity(), false);
    }

    private float ingredientQuantityUsedInCart(Ingredient ingredient, List<OrderItem> cartOrders) {
        float quantity = 0;
        for (OrderItem orderItem : cartOrders) {
            Item item = itemRepository.findById(orderItem.getItem().getId()).orElseThrow(() -> new NoSuchElementException("Not found"));
            for (ItemIngredient itemIngredient : item.getItemIngredients()) {
                ItemIngredient itemIng;
                if (itemIngredient.getType().equals(IngredientType.PRIMARY)) {
                    itemIng = itemIngredientRepository.getItemIngredientByIngredient_IdAndItem_IdAndType(
                            itemIngredient.getIngredient().getId(),
                            orderItem.getItem().getId(),
                            IngredientType.PRIMARY);
                } else {
                    itemIng = itemIngredientRepository.getItemIngredientByIngredient_IdAndItem_IdAndType(
                            itemIngredient.getIngredient().getId(),
                            orderItem.getItem().getId(),
                            IngredientType.SECONDARY);
                }
                if (itemIng.getIngredient().getId() == ingredient.getId()) {
                    if (itemIng.getType().equals(IngredientType.PRIMARY)) {
                        quantity += itemIng.getQuantity() * orderItem.getQuantity();
                    } else if (orderItem.getIngredients().stream().anyMatch(ingredient1 -> ingredient1.getId() == itemIng.getIngredient().getId())) {
                        quantity += itemIng.getQuantity() * orderItem.getQuantity();
                    }
                }
            }
        }
        return quantity;
    }
}
