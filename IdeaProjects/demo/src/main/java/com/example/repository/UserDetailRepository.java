package com.example.repository;

import com.example.entity.UserDetail;
import com.example.entity.UserAuth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserDetailRepository extends JpaRepository<UserDetail, Long> {

    @Query("SELECT ud FROM UserDetail ud WHERE ud.user.id = :userId")
    Optional<UserDetail> findByUserId(@Param("userId") Long userId);

    @Query("SELECT ud FROM UserDetail ud " +
            "LEFT JOIN FETCH ud.user " +
            "LEFT JOIN FETCH ud.city c " +
            "LEFT JOIN FETCH c.state " +
            "WHERE ud.user.id = :userId")
    Optional<UserDetail> findByUserIdWithDetails(@Param("userId") Long userId);

    @Query("SELECT ud FROM UserDetail ud " +
            "LEFT JOIN FETCH ud.user " +
            "LEFT JOIN FETCH ud.city c " +
            "LEFT JOIN FETCH c.state " +
            "WHERE ud.id = :userDetailId")
    Optional<UserDetail> findByIdWithDetails(@Param("userDetailId") Long userDetailId);
    Optional<UserDetail> findByUser(UserAuth user);
}