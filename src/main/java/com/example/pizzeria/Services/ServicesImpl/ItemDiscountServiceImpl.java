package com.example.pizzeria.Services.ServicesImpl;

import com.example.pizzeria.DTOs.ItemDiscountDTO;
import com.example.pizzeria.DTOs.Mappers.ItemDiscountDTOMapper;
import com.example.pizzeria.Entities.ItemDiscount;
import com.example.pizzeria.Repositories.ItemDiscountRepository;
import com.example.pizzeria.Services.ItemDiscountService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ItemDiscountServiceImpl implements ItemDiscountService {

    private final ItemDiscountRepository itemDiscountRepository;
    private final ItemDiscountDTOMapper itemDiscountDTOMapper;

    @Override
    public ItemDiscount addItemDiscount(ItemDiscount itemDiscount) {
        return itemDiscountRepository.save(itemDiscount);
    }

    @Override
    public List<ItemDiscountDTO> getAllItemDiscounts() {
        return itemDiscountRepository.findAll().stream().map(itemDiscountDTOMapper).collect(Collectors.toList());
    }

    @Override
    public ItemDiscountDTO getItemDiscountById(Long itemDiscountId) {
        return itemDiscountDTOMapper.apply(itemDiscountRepository.findById(itemDiscountId)
                .orElseThrow(() -> new NoSuchElementException("No itemDiscount found with id: "+itemDiscountId)));
    }

    @Override
    public ItemDiscountDTO updateItemDiscount(ItemDiscountDTO itemDiscountDTO) {
        ItemDiscount itemDiscount = itemDiscountRepository.findById(itemDiscountDTO.id())
                .orElseThrow(() -> new NoSuchElementException("No itemDiscount found with id: "+itemDiscountDTO.id()));
        itemDiscount.setSize(itemDiscountDTO.size());
        itemDiscount.setQuantity(itemDiscount.getQuantity());
        itemDiscountRepository.save(itemDiscount);
        return itemDiscountDTO;
    }

    @Override
    public void deleteItemDiscount(Long itemDiscountId) {
        itemDiscountRepository.deleteById(itemDiscountId);
    }
}
