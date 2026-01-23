package com.example.service;

import com.example.dto.StateMasterDTO;
import com.example.repository.StateRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StateService {

    // 1. Initialize the Logger
    private static final Logger logger = LoggerFactory.getLogger(StateService.class);

    private final StateRepository stateRepository;

    public StateService(StateRepository stateRepository) {
        this.stateRepository = stateRepository;
    }

    public List<StateMasterDTO> getAllStateNames() {
        logger.info("Fetching all states from the database...");

        List<StateMasterDTO> states = stateRepository.findAll().stream()
                .map(state -> new StateMasterDTO(state.getId(), state.getStateName()))
                .collect(Collectors.toList());

        // 2. Log the outcome
        logger.info("Successfully retrieved {} states.", states.size());

        return states;
    }
}