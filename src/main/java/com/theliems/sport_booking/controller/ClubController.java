package com.theliems.sport_booking.controller;

import com.theliems.sport_booking.model.Club;
import com.theliems.sport_booking.model.ClubSportType;
import com.theliems.sport_booking.model.Court;
import com.theliems.sport_booking.model.SportType;
import com.theliems.sport_booking.repository.ClubRepository;
import com.theliems.sport_booking.repository.ClubSportTypeRepository;
import com.theliems.sport_booking.repository.CourtRepository;
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

    public ClubController(ClubService clubService) {
        this.clubService = clubService;
    }

    // ================= USER / GUEST =================

    @GetMapping
    public List<Club> getAllClubs() {
        return clubService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getClubById(@PathVariable int id) {
        Club club = clubService.getById(id);
        if (club == null || Boolean.TRUE.equals(club.getIsDeleted())) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Không tìm thấy club");
        }
        return ResponseEntity.ok(club);
    }

    // ================= ADMIN =================

    @PostMapping
    public ResponseEntity<?> createClub(@RequestBody Club club) {
        clubService.create(club);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body("Tạo club thành công");
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateClub(
            @PathVariable int id,
            @RequestBody Club club
    ) {
        Club updated = clubService.update(id, club);
        if (updated == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Club không tồn tại");
        }
        return ResponseEntity.ok("Cập nhật club thành công");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteClub(@PathVariable int id) {
        boolean deleted = clubService.softDelete(id);
        if (!deleted) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Club không tồn tại");
        }
        return ResponseEntity.ok("Xóa club thành công");
    }

    // ================= SPORT TYPE =================

    @GetMapping("/{clubId}/sport-types")
    public ResponseEntity<?> getSportTypes(@PathVariable int clubId) {
        return ResponseEntity.ok(
                clubService.getSportTypesByClub(clubId)
        );
    }

    @PostMapping("/{clubId}/sport-types")
    public ResponseEntity<?> addSportTypeToClub(
            @PathVariable int clubId,
            @RequestBody Map<String, Integer> body
    ) {
        clubService.addSportTypeToClub(
                clubId,
                body.get("sportTypeId")
        );
        return ResponseEntity.ok("Gán loại sân cho club thành công");
    }

    @DeleteMapping("/{clubId}/sport-types/{sportTypeId}")
    public ResponseEntity<?> removeSportTypeFromClub(
            @PathVariable int clubId,
            @PathVariable int sportTypeId
    ) {
        clubService.removeSportTypeFromClub(clubId, sportTypeId);
        return ResponseEntity.ok("Đã gỡ loại sân khỏi club");
    }

    // ================= COURT =================

    @GetMapping("/{clubId}/sport-types/{sportTypeId}/courts")
    public ResponseEntity<?> getCourtsBySportType(
            @PathVariable int clubId,
            @PathVariable int sportTypeId
    ) {
        return ResponseEntity.ok(
                clubService.getCourtsByClubAndSportType(clubId, sportTypeId)
        );
    }
}
