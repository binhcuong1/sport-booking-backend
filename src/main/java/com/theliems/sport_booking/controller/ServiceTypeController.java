package com.theliems.sport_booking.controller;

import com.theliems.sport_booking.model.ServiceType;
import com.theliems.sport_booking.service.ServiceTypeService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/service-types")
public class ServiceTypeController {

    private final ServiceTypeService service;

    public ServiceTypeController(ServiceTypeService service) {
        this.service = service;
    }

    @GetMapping
    public List<ServiceType> getAll() {
        return service.getAll();
    }
}
