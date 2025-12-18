package com.theliems.sport_booking.controller;

import com.theliems.sport_booking.model.ClubService;
import com.theliems.sport_booking.service.ClubServiceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/club-service")
public class ClubServiceController {

    private final ClubServiceService service;

    public ClubServiceController(ClubServiceService service) {
        this.service = service;
    }

    // LIST
    @GetMapping("/{clubId}")
    public ResponseEntity<?> list(@PathVariable Integer clubId) {
        return ResponseEntity.ok(service.getByClub(clubId));
    }

    // CREATE
    @PostMapping("/{clubId}")
    public ResponseEntity<?> create(@PathVariable Integer clubId,
                                    @RequestBody ClubService body) {
        body.setClubId(clubId);
        return ResponseEntity.ok(service.create(body));
    }

    // UPDATE
    @PutMapping("/{clubId}/{serviceTypeId}")
    public ResponseEntity<?> update(@PathVariable Integer clubId,
                                    @PathVariable Integer serviceTypeId,
                                    @RequestBody ClubService body) {
        ClubService updated = service.update(clubId, serviceTypeId, body);
        if (updated == null) return ResponseEntity.status(404).body("Not found");
        return ResponseEntity.ok(updated);
    }

    // DELETE (soft)
    @DeleteMapping("/{clubId}/{serviceTypeId}")
    public ResponseEntity<?> delete(@PathVariable Integer clubId,
                                    @PathVariable Integer serviceTypeId) {
        service.softDelete(clubId, serviceTypeId);
        return ResponseEntity.ok("OK");
    }
}
