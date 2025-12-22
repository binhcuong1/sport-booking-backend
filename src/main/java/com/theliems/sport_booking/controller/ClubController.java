package com.theliems.sport_booking.controller;

import com.theliems.sport_booking.model.Club;
import com.theliems.sport_booking.model.ClubSportType;
import com.theliems.sport_booking.model.SportType;
import com.theliems.sport_booking.repository.ClubRepository;
import com.theliems.sport_booking.repository.ClubSportTypeRepository;
import com.theliems.sport_booking.service.ClubService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/clubs")
public class ClubController {

    private final ClubService clubService;
    private final ClubRepository clubRepository;
    private final ClubSportTypeRepository clubSportTypeRepo;

    public ClubController(
            ClubService clubService,
            ClubRepository clubRepository,
            ClubSportTypeRepository clubSportTypeRepo) {
        this.clubService = clubService;
        this.clubRepository = clubRepository;
        this.clubSportTypeRepo = clubSportTypeRepo;
    }

    // GET ALL
    @GetMapping
    public List<Club> getAllClubs() {
        return clubService.getAllClub();
    }

    // GET BY ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getClubById(@PathVariable int id) {
        Club club = clubService.getClubById(id);
        if (club == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Không tìm thấy club");
        }
        return ResponseEntity.ok(club);
    }

    // CREATE
    @PostMapping
    public ResponseEntity<?> createClub(@RequestBody Club club) {
        clubService.createClub(club);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body("Tạo club thành công");
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<?> updateClub(
            @PathVariable int id,
            @RequestBody Club club
    ) {
        Club existing = clubService.getClubById(id);
        if (existing == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Club không tồn tại");
        }

        club.setClubId(id);
        clubService.updateClub(club);
        return ResponseEntity.ok("Cập nhật club thành công");
    }

    // DELETE (soft delete)
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteClub(@PathVariable int id) {
        Club club = clubService.getClubById(id);
        if (club == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Club không tồn tại");
        }

        clubService.deleteClub(id);
        return ResponseEntity.ok("Xóa club thành công");
    }

    // Lấy sport types của club
    @GetMapping("/{clubId}/sport-types")
    public List<SportType> getSportTypesByClub(@PathVariable int clubId) {
        return clubRepository.findSportTypesByClubId(clubId);
    }

    // Thêm sport type cho club
    @PostMapping("/{clubId}/sport-types")
    public ResponseEntity<?> addSportTypeToClub(
            @PathVariable int clubId,
            @RequestBody Map<String, Integer> body
    ) {
        int sportTypeId = body.get("sportTypeId");

        if (clubSportTypeRepo.existsByClubIdAndSportTypeId(clubId, sportTypeId)) {
            return ResponseEntity.badRequest().body("Club đã có loại sân này");
        }

        ClubSportType cst = new ClubSportType();
        cst.setClubId(clubId);
        cst.setSportTypeId(sportTypeId);
        clubSportTypeRepo.save(cst);

        return ResponseEntity.ok("Gán loại sân cho club thành công");
    }

    // Gỡ sport type khỏi club
    @DeleteMapping("/{clubId}/sport-types/{sportTypeId}")
    public ResponseEntity<?> removeSportTypeFromClub(
            @PathVariable int clubId,
            @PathVariable int sportTypeId
    ) {
        int courtCount =
                clubSportTypeRepo.countCourtByClubAndSportType(clubId, sportTypeId);

        if (courtCount > 0) {
            return ResponseEntity
                    .badRequest()
                    .body("Không thể xóa: vẫn còn court thuộc loại sân này");
        }

        clubSportTypeRepo.deleteByClubIdAndSportTypeId(clubId, sportTypeId);
        return ResponseEntity.ok("Đã gỡ loại sân khỏi club");
    }
}