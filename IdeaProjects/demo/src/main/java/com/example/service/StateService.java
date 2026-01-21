package com.example.service;

import com.example.dto.StateMasterDTO;
import com.example.repository.StateRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StateService {

    private final StateRepository stateRepository;

    public StateService(StateRepository stateRepository) {
        this.stateRepository = stateRepository;
    }

    public List<StateMasterDTO> getAllStateNames() {
        return stateRepository.findAll().stream()
                .map(state -> new StateMasterDTO(state.getId(), state.getStateName()))
                .collect(Collectors.toList());
    }
}