package com.example.controllers;

import com.example.entity.DiscountOffer;
import com.example.repository.DiscountOfferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/offers")
@CrossOrigin(origins = "*")
public class DiscountOfferController {

    @Autowired
    private DiscountOfferRepository discountOfferRepository;

    /**
     * Get all discount offers
     * GET /api/offers
     */
    @GetMapping
    public ResponseEntity<List<DiscountOffer>> getAllOffers() {
        List<DiscountOffer> offers = discountOfferRepository.findAll();
        return ResponseEntity.ok(offers);
    }
}
