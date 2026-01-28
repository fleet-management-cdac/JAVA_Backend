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

        @Query("SELECT d FROM DiscountOffer d WHERE :date BETWEEN d.startDate AND d.endDate AND d.isActive = true ORDER BY d.discountPercentage DESC")
        List<DiscountOffer> findApplicableOffers(@Param("date") LocalDate date);

}
