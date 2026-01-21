package com.example.repository;

import com.example.entity.Addon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddonRepository extends JpaRepository<Addon, Long> {
    // JpaRepository provides:
    // - findAll() → returns List<Addon>
    // - findById(Long id) → returns Optional<Addon>
}