package com.example.repository;

import com.example.entity.InvoiceHeader;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InvoiceRepository extends JpaRepository<InvoiceHeader, Long> {
    @Query("SELECT ih FROM InvoiceHeader ih WHERE ih.bookingCustomer.booking.id = :bookingId ORDER BY ih.id DESC")
    List<InvoiceHeader> findByBookingId(@Param("bookingId") Long bookingId);
}