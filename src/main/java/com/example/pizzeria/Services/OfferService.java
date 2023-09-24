package com.example.pizzeria.Services;

import com.example.pizzeria.DTOs.OfferDTO;
import com.example.pizzeria.Entities.Image;
import com.example.pizzeria.Entities.Offer;
import org.springframework.data.domain.Page;

import java.util.List;

public interface OfferService {

    OfferDTO addOffer(Offer offer);
    Page<OfferDTO> getAllOffers(Integer pageNumber, Integer pageSize);
    List<OfferDTO> getAllAvailableOffers();
    OfferDTO getOfferById(Long offerId);
    OfferDTO updateOffer(OfferDTO offerDTO, Image image);
    void deleteOffer(Long offerId);
}
