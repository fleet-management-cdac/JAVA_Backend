package com.example.repository;

import com.example.entity.BookingCustomerDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookingCustomerDetailRepository extends JpaRepository<BookingCustomerDetail, Long> {
    Optional<BookingCustomerDetail> findByBookingId(Long bookingId);
}