package com.example.repository;

import com.example.entity.StateMaster;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StateRepository extends JpaRepository<StateMaster, Long> {
}