package com.example.repository;

import com.example.entity.Handover;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HandoverRepository extends JpaRepository<Handover, Long> {

    @Query("SELECT h FROM Handover h WHERE h.booking.id = :bookingId")
    List<Handover> findByBookingId(@Param("bookingId") Long bookingId);

    @Query("SELECT h FROM Handover h " +
            "LEFT JOIN FETCH h.booking b " +
            "LEFT JOIN FETCH h.vehicle " +
            "LEFT JOIN FETCH h.processedBy " +
            "WHERE h.id = :handoverId")
    Optional<Handover> findByIdWithDetails(@Param("handoverId") Long handoverId);
}