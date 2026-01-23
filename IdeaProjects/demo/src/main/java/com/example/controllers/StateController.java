package com.example.controllers;

import com.example.dto.StateMasterDTO;
import com.example.service.StateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/locations")
@CrossOrigin(origins = "*")
public class StateController {

    // 1. Define the Logger
    private static final Logger logger = LoggerFactory.getLogger(StateController.class);

    @Autowired
    private StateService stateService;

    @GetMapping("/states")
    public List<StateMasterDTO> getStates() {
        // 2. Add a log entry
        logger.info("REST request to get all states");

        List<StateMasterDTO> states = stateService.getAllStateNames();

        // Optional: log the count of states found
        logger.debug("Found {} states in the database", states.size());

        return states;
    }
}