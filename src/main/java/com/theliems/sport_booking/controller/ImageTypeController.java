package com.theliems.sport_booking.controller;

import com.theliems.sport_booking.model.ImageType;
import com.theliems.sport_booking.service.ImageTypeService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/image-types")
public class ImageTypeController {

    private final ImageTypeService service;

    public ImageTypeController(ImageTypeService service) {
        this.service = service;
    }

    @GetMapping
    public List<ImageType> getAll() {
        return service.getAll();
    }
}
