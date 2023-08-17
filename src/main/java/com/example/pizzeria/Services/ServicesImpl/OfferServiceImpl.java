package com.example.pizzeria.Services.ServicesImpl;

import com.example.pizzeria.DTOs.Mappers.OfferDTOMapper;
import com.example.pizzeria.DTOs.OfferDTO;
import com.example.pizzeria.Entities.Offer;
import com.example.pizzeria.Repositories.OfferRepository;
import com.example.pizzeria.Services.OfferService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class OfferServiceImpl implements OfferService {

    private final OfferRepository offerRepository;
    private final OfferDTOMapper offerDTOMapper;

    @Override
    public OfferDTO addOffer(Offer offer) {
        return offerDTOMapper.apply(offerRepository.save(offer));
    }

    @Override
    public List<OfferDTO> getAllOffers() {
        return offerRepository.findAll().stream().map(offerDTOMapper).collect(Collectors.toList());
    }

    @Override
    public OfferDTO getOfferById(Long offerId) {
        return offerDTOMapper.apply(offerRepository.findById(offerId)
                .orElseThrow(() -> new NoSuchElementException("No offer found with id: " + offerId)));
    }

    @Override
    public OfferDTO updateOffer(OfferDTO offerDTO) {
        Offer offer = offerRepository.findById(offerDTO.id())
                .orElseThrow(() -> new NoSuchElementException("No offer found with id: " + offerDTO.id()));
        offer.setBeginDate(offerDTO.beginDate());
        offer.setEndDate(offerDTO.endDate());
        offer.setDescription(offerDTO.description());
        offer.setImage(offerDTO.image());
        offer.setTotalPrice(offerDTO.totalPrice());
        offerRepository.save(offer);
        return offerDTO;
    }

    @Override
    public void deleteOffer(Long offerId) {
        offerRepository.deleteById(offerId);
    }
}
