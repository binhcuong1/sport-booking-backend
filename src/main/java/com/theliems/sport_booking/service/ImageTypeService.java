package com.theliems.sport_booking.service;

import com.theliems.sport_booking.model.ImageType;
import com.theliems.sport_booking.repository.ImageTypeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ImageTypeService {
    private final ImageTypeRepository repo;

    public ImageTypeService(ImageTypeRepository repo) {
        this.repo = repo;
    }

    public List<ImageType> getAll() {
        return repo.findAll();
    }
}
