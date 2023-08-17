package com.example.pizzeria.Services;

import com.example.pizzeria.DTOs.OfferDTO;
import com.example.pizzeria.Entities.Offer;

import java.util.List;

public interface OfferService {

    OfferDTO addOffer(Offer offer);
    List<OfferDTO> getAllOffers();
    OfferDTO getOfferById(Long offerId);
    OfferDTO updateOffer(OfferDTO offerDTO);
    void deleteOffer(Long offerId);
}
