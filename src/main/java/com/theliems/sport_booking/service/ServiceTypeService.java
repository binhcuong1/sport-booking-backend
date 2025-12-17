package com.theliems.sport_booking.service;

import com.theliems.sport_booking.model.ServiceType;
import com.theliems.sport_booking.repository.ServiceTypeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServiceTypeService {

    private final ServiceTypeRepository repo;

    public ServiceTypeService(ServiceTypeRepository repo) {
        this.repo = repo;
    }

    public List<ServiceType> getAll() {
        return repo.findAll();
    }
}
