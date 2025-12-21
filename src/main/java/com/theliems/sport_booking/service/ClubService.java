package com.theliems.sport_booking.service;

import com.theliems.sport_booking.model.Club;
import com.theliems.sport_booking.repository.ClubRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClubService {

    private final ClubRepository clubRepository;

    public ClubService(ClubRepository clubRepository) {
        this.clubRepository = clubRepository;
    }

    public List<Club> getAllClub() {
        return clubRepository.findByIsDeletedFalse();
    }

    public Club getClubById(int id) {
        return clubRepository.findByClubIdAndIsDeletedFalse(id);
    }

    public void createClub(Club club) {
        if (!clubRepository.existsByClubNameIgnoreCase(club.getClubName())) {
            club.setClubId(null);
            club.setIsDeleted(false);
            clubRepository.save(club);
        }
    }

    public void updateClub(Club club) {
        Club existing = getClubById(club.getClubId());
        if (existing != null) {
            existing.setClubName(club.getClubName());
            existing.setDescription(club.getDescription());
            existing.setAddress(club.getAddress());
            existing.setOpenTime(club.getOpenTime());
            existing.setCloseTime(club.getCloseTime());
            existing.setContactPhone(club.getContactPhone());
            clubRepository.save(existing);
        }
    }

    public void deleteClub(int id) {
        Club club = getClubById(id);
        if (club != null) {
            club.setIsDeleted(true); // soft delete
            clubRepository.save(club);
        }
    }
}