package com.example.controllers;

import com.example.dto.StateMasterDTO;
import com.example.service.StateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/locations")
@CrossOrigin(origins = "*")
public class StateController {

    @Autowired
    private StateService stateService;

    @GetMapping("/states")
    public List<StateMasterDTO> getStates() {
        return stateService.getAllStateNames();
    }
}