package com.example.pizzeria.Controllers;

import com.example.pizzeria.DTOs.OfferDTO;
import com.example.pizzeria.Entities.Offer;
import com.example.pizzeria.Services.OfferService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/offer")
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class OfferController {

    private final OfferService offerService;

    @GetMapping("/all")
    public List<OfferDTO> getAllOffers(){
        return offerService.getAllOffers();
    }
    @PostMapping("/add")
    public OfferDTO addOffer(@RequestBody Offer offer){
        return offerService.addOffer(offer);
    }
    @GetMapping("/{id}")
    public OfferDTO getOfferById(@PathVariable("id") Long offerId){
        return offerService.getOfferById(offerId);
    }
    @PutMapping("/update")
    public OfferDTO updateOffer(@RequestBody OfferDTO offerDTO){
        return offerService.updateOffer(offerDTO);
    }
    @DeleteMapping("/delete/{id}")
    public void deleteOffer(@PathVariable("id") Long offerId){
        offerService.deleteOffer(offerId);
    }
}
