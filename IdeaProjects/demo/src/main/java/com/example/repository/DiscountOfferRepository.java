package com.example.repository;



import com.example.entity.DiscountOffer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DiscountOfferRepository extends JpaRepository<DiscountOffer, Long> {


        /**
         * Finds offers that were ACTIVE when the customer picked up the vehicle
         *
         * Business Rule: Customer must have known about the offer at booking time
         *
         * Logic: Offer applies if pickup date falls within offer period
         */
        @Query("SELECT d FROM DiscountOffer d " +
                "WHERE d.isActive = true " +
                "AND :pickupDate BETWEEN d.startDate AND d.endDate " +
                "ORDER BY d.discountPercentage DESC")
        List<DiscountOffer> findApplicableOffers(
                @Param("pickupDate") LocalDate pickupDate
        );

}
