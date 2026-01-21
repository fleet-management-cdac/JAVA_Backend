package com.example.service;

import com.example.entity.Addon;
import com.example.repository.AddonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class AddonService {

    @Autowired
    private AddonRepository addonRepository;

    // GET all addons
    public List<Addon> getAllAddons() {
        return addonRepository.findAll();
    }

    // GET addon by ID
    public Optional<Addon> getAddonById(Long id) {
        return addonRepository.findById(id);
    }
}