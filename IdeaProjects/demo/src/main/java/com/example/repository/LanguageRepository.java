package com.example.repository;

import com.example.entity.Language;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LanguageRepository extends JpaRepository<Language, Long> {

    Optional<Language> findByCode(String code);

    Optional<Language> findByIsDefaultTrue();

    boolean existsByCode(String code);
}