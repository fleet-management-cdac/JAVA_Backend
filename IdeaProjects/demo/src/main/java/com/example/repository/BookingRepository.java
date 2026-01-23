package com.example.repository;

import com.example.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    // Fixes: cannot find symbol method findByUserId(java.lang.Long)
    List<Booking> findByUserId(Long userId);

    // Fixes: cannot find symbol method findByIdWithDetails(java.lang.Long)
    // We use JOIN FETCH to load customer, vehicle, and rate in one query (optimized)
    @Query("SELECT b FROM Booking b " +
            "LEFT JOIN FETCH b.bookingCustomerDetail " +
            "LEFT JOIN FETCH b.vehicle " +
            "LEFT JOIN FETCH b.rate " +
            "WHERE b.id = :id")
    Optional<Booking> findByIdWithDetails(@Param("id") Long id);
}