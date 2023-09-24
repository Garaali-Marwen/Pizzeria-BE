package com.example.pizzeria.Services.ServicesImpl;

import com.example.pizzeria.DTOs.ItemDiscountDTO;
import com.example.pizzeria.DTOs.Mappers.OfferDTOMapper;
import com.example.pizzeria.DTOs.OfferDTO;
import com.example.pizzeria.Entities.Image;
import com.example.pizzeria.Entities.ItemDiscount;
import com.example.pizzeria.Entities.Offer;
import com.example.pizzeria.Repositories.ItemDiscountRepository;
import com.example.pizzeria.Repositories.ItemRepository;
import com.example.pizzeria.Repositories.OfferRepository;
import com.example.pizzeria.Services.ImageService;
import com.example.pizzeria.Services.ItemDiscountService;
import com.example.pizzeria.Services.OfferService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class OfferServiceImpl implements OfferService {

    private final OfferRepository offerRepository;
    private final OfferDTOMapper offerDTOMapper;
    private final ItemDiscountRepository itemDiscountRepository;
    private final ImageService imageService;
    private final ItemRepository itemRepository;

    @Override
    public OfferDTO addOffer(Offer offer) {
        List<ItemDiscount> itemDiscounts = itemDiscountRepository.saveAll(offer.getItemsDiscount());
        offer.setItemsDiscount(itemDiscounts);
        Offer newOffer = offerRepository.save(offer);
        itemDiscounts.forEach(itemDiscount -> itemDiscount.setOffer(newOffer));
        itemDiscountRepository.saveAll(itemDiscounts);
        return offerDTOMapper.apply(offerRepository.save(offer));
    }

    @Override
    public Page<OfferDTO> getAllOffers(Integer pageNumber, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return offerRepository.findAll(pageable).map(offerDTOMapper);
    }

    @Override
    public List<OfferDTO> getAllAvailableOffers() {
        Date currentDate = new Date();
        return offerRepository.findByBeginDateLessThanEqualAndEndDateGreaterThanEqual(currentDate, currentDate).stream().map(offerDTOMapper).collect(Collectors.toList());
    }

    @Override
    public OfferDTO getOfferById(Long offerId) {
        return offerDTOMapper.apply(offerRepository.findById(offerId)
                .orElseThrow(() -> new NoSuchElementException("No offer found with id: " + offerId)));
    }

    @Override
    public OfferDTO updateOffer(OfferDTO offerDTO, Image image) {
        Offer offer = offerRepository.findById(offerDTO.id())
                .orElseThrow(() -> new NoSuchElementException("No offer found with id: " + offerDTO.id()));
        Image imageDelete = offer.getImage();

        offer.setBeginDate(offerDTO.beginDate());
        offer.setEndDate(offerDTO.endDate());
        offer.setDescription(offerDTO.description());
        offer.setTotalPrice(offerDTO.totalPrice());

        List<Long> updatedItemDiscountIds = offerDTO.itemsDiscount().stream()
                .map(ItemDiscountDTO::id)
                .toList();

        for (ItemDiscountDTO itemDiscountDTO : offerDTO.itemsDiscount()) {
            ItemDiscount itemDiscount = new ItemDiscount();
            if (itemDiscountDTO.id() != null)
                itemDiscount = itemDiscountRepository.findById(itemDiscountDTO.id())
                        .orElseThrow(() -> new NoSuchElementException("No itemDiscount found with id: " + itemDiscountDTO.id()));

            itemDiscount.setSize(itemDiscountDTO.size());
            itemDiscount.setQuantity(itemDiscountDTO.quantity());
            itemDiscount.setItem(itemRepository.findById(itemDiscountDTO.item().id()).orElseThrow(() -> new NoSuchElementException("not found")));
            itemDiscount.setOffer(offer);
            itemDiscountRepository.save(itemDiscount);

            if (offer.getItemsDiscount()
                    .stream()
                    .noneMatch(itemDiscount1 -> itemDiscount1.getId().equals(itemDiscountDTO.id()))) {
                offer.getItemsDiscount().add(itemDiscount);
            }
        }

        if (image != null)
            offer.setImage(image);
        Offer newOffer = offerRepository.save(offer);

        if (image != null)
            imageService.deleteImage(imageDelete.getId());

        for (ItemDiscount itemDiscount : newOffer.getItemsDiscount())
        {
            if (!updatedItemDiscountIds.contains(itemDiscount.getId())) {
                itemDiscount.setOffer(null);
                itemDiscount.setSize(null);
                itemDiscountRepository.save(itemDiscount);
                itemDiscountRepository.deleteById(itemDiscount.getId());
            }
        }
        return offerDTO;
    }

    @Override
    public void deleteOffer(Long offerId) {
        offerRepository.deleteById(offerId);
    }
}
