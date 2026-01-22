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

    @Query("SELECT b FROM Booking b " +
            "LEFT JOIN FETCH b.user " +
            "LEFT JOIN FETCH b.vehicle " +
            "LEFT JOIN FETCH b.rate " +
            "LEFT JOIN FETCH b.pickupHub " +
            "LEFT JOIN FETCH b.returnHub " +
            "LEFT JOIN FETCH b.bookingCustomerDetail cd " +
            "LEFT JOIN FETCH cd.city c " +
            "LEFT JOIN FETCH c.state " +
            "WHERE b.id = :bookingId")
    Optional<Booking> findByIdWithDetails(@Param("bookingId") Long bookingId);

    @Query("SELECT b FROM Booking b " +
            "LEFT JOIN FETCH b.vehicle v " +
            "LEFT JOIN FETCH b.bookingCustomerDetail " +
            "WHERE b.user.id = :userId")
    List<Booking> findByUserId(@Param("userId") Long userId);

    List<Booking> findByStatus(String status);
}