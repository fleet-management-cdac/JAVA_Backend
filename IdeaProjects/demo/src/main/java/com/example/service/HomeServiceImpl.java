package com.example.service;

import com.example.dto.StateDto;
import com.example.repository.StateRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HomeServiceImpl implements HomeService {

    private final StateRepository stateRepository;

    public HomeServiceImpl(StateRepository stateRepository) {
        this.stateRepository = stateRepository;
    }

    @Override
    public List<StateDto> getAllStates() {
        return stateRepository.findAll()
                .stream()
                .map(s -> new StateDto(s.getId(), s.getStateName()))
                .toList();
    }
}