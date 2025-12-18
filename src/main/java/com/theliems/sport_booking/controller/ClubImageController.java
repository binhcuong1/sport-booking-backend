package com.theliems.sport_booking.controller;

import com.theliems.sport_booking.model.ClubImage;
import com.theliems.sport_booking.service.ClubImageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/club-image")
public class ClubImageController {

    private final ClubImageService service;

    public ClubImageController(ClubImageService service) {
        this.service = service;
    }

    @GetMapping("/{clubId}")
    public ResponseEntity<?> list(@PathVariable Integer clubId) {
        return ResponseEntity.ok(service.getByClub(clubId));
    }

    @GetMapping("/{clubId}/{imageTypeId}")
    public ResponseEntity<?> getOne(@PathVariable Integer clubId,
                                    @PathVariable Integer imageTypeId) {
        ClubImage ci = service.getOne(clubId, imageTypeId);
        if (ci == null) return ResponseEntity.status(404).body("Not found");
        return ResponseEntity.ok(ci);
    }

    @PostMapping("/{clubId}")
    public ResponseEntity<?> create(@PathVariable Integer clubId,
                                    @RequestBody ClubImage body) {
        try {
            return ResponseEntity.ok(service.create(clubId, body));
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @PutMapping("/{clubId}/{imageTypeId}")
    public ResponseEntity<?> update(@PathVariable Integer clubId,
                                    @PathVariable Integer imageTypeId,
                                    @RequestBody ClubImage body) {
        ClubImage updated = service.update(clubId, imageTypeId, body);
        if (updated == null) return ResponseEntity.status(404).body("Not found");
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{clubId}/{imageTypeId}")
    public ResponseEntity<?> delete(@PathVariable Integer clubId,
                                    @PathVariable Integer imageTypeId) {
        service.softDelete(clubId, imageTypeId);
        return ResponseEntity.ok("OK");
    }
}
