package com.example.repository;

import com.example.entity.InvoiceHeader;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InvoiceHeaderRepository extends JpaRepository<InvoiceHeader, Long> {

    // Fixed: Changed bookingCustomerDetail to bookingCustomer
    @Query("SELECT i FROM InvoiceHeader i " +
            "LEFT JOIN FETCH i.bookingCustomer bcd " +
            "LEFT JOIN FETCH bcd.booking b " +
            "LEFT JOIN FETCH b.vehicle " +
            "WHERE i.id = :invoiceId")
    Optional<InvoiceHeader> findByIdWithDetails(@Param("invoiceId") Long invoiceId);

    // Fixed: Changed bookingCustomerDetail to bookingCustomer
    @Query("SELECT i FROM InvoiceHeader i " +
            "WHERE i.bookingCustomer.booking.id = :bookingId")
    Optional<InvoiceHeader> findByBookingId(@Param("bookingId") Long bookingId);
}