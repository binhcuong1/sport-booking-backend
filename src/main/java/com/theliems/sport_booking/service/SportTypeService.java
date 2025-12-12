package com.theliems.sport_booking.service;

import com.theliems.sport_booking.model.SportType;
import com.theliems.sport_booking.repository.SportTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SportTypeService {

    @Autowired
    private SportTypeRepository repository;

    public List<SportType> getAll() {
        return repository.findAll();
    }

    public SportType getById(Integer id) {
        return repository.findById(id).orElse(null);
    }

    public SportType create(SportType sportType) {
        return repository.save(sportType);
    }

    public SportType update(Integer id, SportType sportType) {
        SportType existing = repository.findById(id).orElse(null);
        if (existing == null) return null;

        existing.setSport_name(sportType.getSport_name());
        existing.setIs_deleted(sportType.getIs_deleted());

        return repository.save(existing);
    }

    public void delete(Integer id) {
        repository.deleteById(id);
    }
}