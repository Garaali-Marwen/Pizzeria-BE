package com.example.pizzeria.DTOs.Mappers;

import com.example.pizzeria.DTOs.OfferDTO;
import com.example.pizzeria.Entities.Offer;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class OfferDTOMapper implements Function<Offer, OfferDTO> {

    private final ItemDiscountDTOMapper itemDiscountDTOMapper;

    @Override
    public OfferDTO apply(Offer offer) {
        return new OfferDTO(
                offer.getId(),
                offer.getDescription(),
                offer.getTotalPrice(),
                offer.getBeginDate(),
                offer.getEndDate(),
                offer.getItemsDiscount().stream().map(itemDiscountDTOMapper).collect(Collectors.toList()),
                offer.getImage()
        );
    }
}
