package com.theliems.sport_booking.service;

import com.theliems.sport_booking.model.*;
import com.theliems.sport_booking.repository.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClubService {

    private final ClubRepository clubRepository;
    private final ClubSportTypeRepository clubSportTypeRepo;
    private final CourtRepository courtRepo;

    // === THÊM ===
    private final CourtScheduleRepository courtScheduleRepository;
    private final ClubServiceRepository clubServiceRepository;
    private final ClubImageRepository clubImageRepository;

    public ClubService(
            ClubRepository clubRepository,
            ClubSportTypeRepository clubSportTypeRepo,
            CourtRepository courtRepo,
            CourtScheduleRepository courtScheduleRepository,
            ClubServiceRepository clubServiceRepository,
            ClubImageRepository clubImageRepository
    ) {
        this.clubRepository = clubRepository;
        this.clubSportTypeRepo = clubSportTypeRepo;
        this.courtRepo = courtRepo;
        this.courtScheduleRepository = courtScheduleRepository;
        this.clubServiceRepository = clubServiceRepository;
        this.clubImageRepository = clubImageRepository;
    }

    // ================= USER =================

    public List<Club> getAll() {

        List<Club> clubs = clubRepository.findByIsDeletedFalse();

        clubs.forEach(club -> {
            club.setImageUrl(
                    clubImageRepository.findCoverByClub(club.getClubId())
            );
        });

        return clubs;
    }


    public Club getById(Integer id) {
        return clubRepository.findById(id)
                .filter(c -> !Boolean.TRUE.equals(c.getIsDeleted()))
                .orElse(null);
    }

    // ================= ADMIN =================

    public Club create(Club club) {

        if (clubRepository.existsByClubNameIgnoreCase(club.getClubName())) {
            throw new RuntimeException("Tên club đã tồn tại");
        }

        if (club.getAddress() == null || club.getAddress().isBlank()) {
            throw new RuntimeException("Địa chỉ không hợp lệ");
        }

        club.setClubId(null);
        club.setIsDeleted(false);

        return clubRepository.save(club);
    }

    public Club update(Integer id, Club club) {

        Club existing = clubRepository.findById(id)
                .filter(c -> !Boolean.TRUE.equals(c.getIsDeleted()))
                .orElse(null);

        if (existing == null) return null;

        existing.setClubName(club.getClubName());
        existing.setDescription(club.getDescription());
        existing.setContactPhone(club.getContactPhone());
        existing.setOpenTime(club.getOpenTime());
        existing.setCloseTime(club.getCloseTime());
        existing.setAddress(club.getAddress());
        existing.setLatitude(club.getLatitude());
        existing.setLongitude(club.getLongitude());

        return clubRepository.save(existing);
    }

    // ================= DELETE =================

    public boolean softDelete(Integer id) {
        Club club = clubRepository.findById(id).orElse(null);
        if (club == null || Boolean.TRUE.equals(club.getIsDeleted())) {
            return false;
        }

        club.setIsDeleted(true);
        clubRepository.save(club);
        return true;
    }

    // ================= SPORT TYPE =================

    public List<SportType> getSportTypesByClub(int clubId) {
        Club club = getById(clubId);
        if (club == null) {
            throw new RuntimeException("Club không tồn tại");
        }
        return clubRepository.findSportTypesByClubId(clubId);
    }

    public void addSportTypeToClub(int clubId, int sportTypeId) {

        Club club = getById(clubId);
        if (club == null) {
            throw new RuntimeException("Club không tồn tại");
        }

        if (clubSportTypeRepo.existsByClubIdAndSportTypeId(clubId, sportTypeId)) {
            throw new RuntimeException("Club đã có sport type này");
        }

        ClubSportType cst = new ClubSportType();
        cst.setClubId(clubId);
        cst.setSportTypeId(sportTypeId);
        clubSportTypeRepo.save(cst);
    }

    public void removeSportTypeFromClub(int clubId, int sportTypeId) {

        Club club = getById(clubId);
        if (club == null) {
            throw new RuntimeException("Club không tồn tại");
        }

        int courtCount =
                clubSportTypeRepo.countCourtByClubAndSportType(clubId, sportTypeId);

        if (courtCount > 0) {
            throw new RuntimeException(
                    "Không thể xóa: vẫn còn court thuộc loại sân này"
            );
        }

        clubSportTypeRepo.deleteByClubIdAndSportTypeId(clubId, sportTypeId);
    }

    // ================= COURT =================

    public List<Court> getCourtsByClubAndSportType(int clubId, int sportTypeId) {

        Club club = getById(clubId);
        if (club == null) {
            throw new RuntimeException("Club không tồn tại");
        }

        return courtRepo.findByClubAndSportType(clubId, sportTypeId);
    }

    // ================= CLUB DETAIL (NO DTO) =================

    public Club getClubDetail(int clubId) {

        Club club = clubRepository.findById(clubId)
                .filter(c -> !Boolean.TRUE.equals(c.getIsDeleted()))
                .orElseThrow(() -> new RuntimeException("Club không tồn tại"));

        // COURTS + SCHEDULE
        club.setCourts(
                courtScheduleRepository.findByCourt_ClubIdAndStatusNot(
                        clubId,
                        CourtSchedule.Status.blocked
                )
        );

        // SERVICES
        club.setServices(
                clubServiceRepository.findByClubIdAndIsDeletedFalse(clubId)
        );

        //  IMAGE COVER
        club.setImageUrl(
                clubImageRepository.findCoverByClub(clubId)
        );

        return club;
    }
}
