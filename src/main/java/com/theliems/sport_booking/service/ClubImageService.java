package com.theliems.sport_booking.service;

import com.theliems.sport_booking.model.ClubImage;
import com.theliems.sport_booking.model.ClubImageId;
import com.theliems.sport_booking.repository.ClubImageRepository;
import com.theliems.sport_booking.repository.ImageTypeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClubImageService {

    private final ClubImageRepository repo;
    private final ImageTypeRepository imageTypeRepo;

    public ClubImageService(ClubImageRepository repo, ImageTypeRepository imageTypeRepo) {
        this.repo = repo;
        this.imageTypeRepo = imageTypeRepo;
    }

    public List<ClubImage> getByClub(Integer clubId) {
        return repo.findByIdClubId(clubId);
    }

    public ClubImage getOne(Integer clubId, Integer imageTypeId) {
        return repo.findById(new ClubImageId(clubId, imageTypeId)).orElse(null);
    }

    public ClubImage create(Integer clubId, ClubImage body) {
        if (body.getId() == null || body.getId().getImageTypeId() == null)
            throw new RuntimeException("imageTypeId is required");

        Integer imageTypeId = body.getId().getImageTypeId();
        body.setId(new ClubImageId(clubId, imageTypeId));

        if (body.getImageUrl() == null || body.getImageUrl().isBlank())
            throw new RuntimeException("imageUrl is required");

        // check ImageType tồn tại
        imageTypeRepo.findById(imageTypeId)
                .orElseThrow(() -> new RuntimeException("ImageType not found"));

        if (repo.existsById(body.getId()))
            throw new RuntimeException("This (clubId, imageTypeId) already exists");

        if (body.getIsDeleted() == null) body.setIsDeleted(false);

        return repo.save(body);
    }

    public ClubImage update(Integer clubId, Integer imageTypeId, ClubImage body) {
        ClubImageId id = new ClubImageId(clubId, imageTypeId);
        ClubImage current = repo.findById(id).orElse(null);
        if (current == null) return null;

        // không đổi key
        if (body.getImageUrl() != null) current.setImageUrl(body.getImageUrl());
        if (body.getIsDeleted() != null) current.setIsDeleted(body.getIsDeleted());

        return repo.save(current);
    }

    public void softDelete(Integer clubId, Integer imageTypeId) {
        ClubImageId id = new ClubImageId(clubId, imageTypeId);
        ClubImage current = repo.findById(id).orElse(null);
        if (current == null) return;
        current.setIsDeleted(true);
        repo.save(current);
    }
}
