package com.theliems.sport_booking.controller;

import com.theliems.sport_booking.model.Court;
import com.theliems.sport_booking.repository.ClubRepository;
import com.theliems.sport_booking.repository.ClubSportTypeRepository;
import com.theliems.sport_booking.repository.CourtRepository;
import com.theliems.sport_booking.repository.SportTypeRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/courts")
public class CourtController {

    private final CourtRepository courtRepo;
    private final ClubRepository clubRepo;
    private final SportTypeRepository sportTypeRepo;
    private final ClubSportTypeRepository clubSportTypeRepo;

    public CourtController(
            CourtRepository courtRepo,
            ClubRepository clubRepo,
            SportTypeRepository sportTypeRepo,
            ClubSportTypeRepository clubSportTypeRepo
    ) {
        this.courtRepo = courtRepo;
        this.clubRepo = clubRepo;
        this.sportTypeRepo = sportTypeRepo;
        this.clubSportTypeRepo = clubSportTypeRepo;
    }

    @PostMapping
    public ResponseEntity<?> createCourt(@RequestBody Court court) {

        if (!clubRepo.existsById(court.getClubId()))
            return ResponseEntity.badRequest().body("Club không tồn tại");

        if (!clubSportTypeRepo.existsByClubIdAndSportTypeId(
                court.getClubId(), court.getSportTypeId()))
            return ResponseEntity.badRequest()
                    .body("Club chưa đăng ký loại sân này");

        if (courtRepo.existsCourtName(
                court.getClubId(),
                court.getSportTypeId(),
                court.getCourtName()) > 0)
            return ResponseEntity.badRequest()
                    .body("Tên sân đã tồn tại");

        court.setCourtId(null);
        court.setIsDeleted(false);
        courtRepo.save(court);

        return ResponseEntity.ok("Tạo court thành công");
    }

    @PutMapping("/{courtId}")
    public ResponseEntity<?> updateCourt(
            @PathVariable int courtId,
            @RequestBody Court body
    ) {
        Court court = courtRepo.findById(courtId).orElse(null);
        if (court == null)
            return ResponseEntity.badRequest().body("Court không tồn tại");

        court.setCourtName(body.getCourtName());
        court.setIsDeleted(body.getIsDeleted());
        courtRepo.save(court);

        return ResponseEntity.ok("Cập nhật court thành công");
    }

    @DeleteMapping("/{courtId}")
    public ResponseEntity<?> deleteCourt(@PathVariable int courtId) {
        Court court = courtRepo.findById(courtId).orElse(null);
        if (court == null)
            return ResponseEntity.badRequest().body("Court không tồn tại");

        court.setIsDeleted(true);
        courtRepo.save(court);

        return ResponseEntity.ok("Đã xóa court");
    }
}
