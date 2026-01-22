package com.example.repository;


import com.example.entity.Translation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TranslationRepository extends JpaRepository<Translation, Long> {

    List<Translation> findByLanguageId(Long languageId);

    Optional<Translation> findByTKeyAndLanguageId(String tKey, Long languageId);

    @Query("SELECT t FROM Translation t WHERE t.tKey = :tKey AND t.language.code = :code")
    Optional<Translation> findByTKeyAndLanguageCode(@Param("tKey") String tKey, @Param("code") String code);

    @Query("SELECT t FROM Translation t WHERE t.language.code = :code")
    List<Translation> findAllByLanguageCode(@Param("code") String code);

    boolean existsByTKeyAndLanguageId(String tKey, Long languageId);

    void deleteAllByTKey(String tKey);
}