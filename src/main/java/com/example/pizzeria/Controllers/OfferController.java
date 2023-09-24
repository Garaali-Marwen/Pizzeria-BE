package com.example.pizzeria.Controllers;

import com.example.pizzeria.DTOs.OfferDTO;
import com.example.pizzeria.Entities.Image;
import com.example.pizzeria.Entities.Item;
import com.example.pizzeria.Entities.Offer;
import com.example.pizzeria.Services.ImageService;
import com.example.pizzeria.Services.OfferService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/offer")
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class OfferController {

    private final OfferService offerService;
    private final ImageService imageService;

    @GetMapping("/all/{pageNumber}/{pageSize}")
    public Page<OfferDTO> getAllOffers(@PathVariable("pageNumber") Integer pageNumber, @PathVariable("pageSize") Integer pageSize){
        return offerService.getAllOffers(pageNumber, pageSize);
    }

    @GetMapping("/all")
    public List<OfferDTO> getAllAvailableOffers(){
        return offerService.getAllAvailableOffers();
    }

    @PostMapping(value = "/add", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public OfferDTO addOffer(@RequestPart("offer") Offer offer,
                             @RequestPart("image") MultipartFile image){
        try {
            Image images = imageService.uploadFile(image);
            offer.setImage(images);
            return offerService.addOffer(offer);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
    @GetMapping("/{id}")
    public OfferDTO getOfferById(@PathVariable("id") Long offerId){
        return offerService.getOfferById(offerId);
    }
    @PutMapping(value = "/update", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public OfferDTO updateOffer(@RequestPart("offer") OfferDTO offerDTO,
                                @RequestPart("image") MultipartFile image){
        try {
            Image images = imageService.uploadFile(image);
            return offerService.updateOffer(offerDTO, images);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
    @DeleteMapping("/delete/{id}")
    public void deleteOffer(@PathVariable("id") Long offerId){
        offerService.deleteOffer(offerId);
    }
}
