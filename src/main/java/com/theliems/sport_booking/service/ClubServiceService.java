package com.theliems.sport_booking.service;

import com.theliems.sport_booking.model.ClubService;
import com.theliems.sport_booking.model.ClubServiceId;
import com.theliems.sport_booking.repository.ClubServiceRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClubServiceService {

    private final ClubServiceRepository repository;

    public ClubServiceService(ClubServiceRepository repository) {
        this.repository = repository;
    }

    public List<ClubServiceRepository.ClubServiceRow> getByClub(Integer clubId) {
        return repository.findRowsByClub(clubId);
    }

    public ClubService getOne(Integer clubId, Integer serviceTypeId) {
        return repository.findById(new ClubServiceId(clubId, serviceTypeId)).orElse(null);
    }

    public ClubService create(ClubService cs) {
        ClubServiceId id = new ClubServiceId(cs.getClubId(), cs.getServiceTypeId());
        if (repository.existsById(id)) {
            throw new RuntimeException("Dịch vụ này đã tồn tại trong club.");
        }
        return repository.save(cs);
    }

    public ClubService update(Integer clubId, Integer serviceTypeId, ClubService data) {
        ClubService existing = getOne(clubId, serviceTypeId);
        if (existing == null) return null;

        existing.setName(data.getName());
        existing.setDescription(data.getDescription());
        existing.setIsDeleted(data.getIsDeleted());

        return repository.save(existing);
    }

    // Soft delete
    public void softDelete(Integer clubId, Integer serviceTypeId) {
        ClubService existing = getOne(clubId, serviceTypeId);
        if (existing == null) return;
        existing.setIsDeleted(true);
        repository.save(existing);
    }
}
