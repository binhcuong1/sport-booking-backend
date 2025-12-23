package com.theliems.sport_booking.controller;

import com.theliems.sport_booking.model.Club;
import com.theliems.sport_booking.service.ClubService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/club")
public class ClubController {

    private final ClubService service;

    public ClubController(ClubService service) {
        this.service = service;
    }




    @GetMapping
    public List<Club> getAll() {
        return service.getAll();
    }

    // GET ONE
    @GetMapping("/{id}")
    public ResponseEntity<?> getOne(@PathVariable Integer id) {
        Club club = service.getById(id);
        if (club == null || Boolean.TRUE.equals(club.getIsDeleted())) {
            return ResponseEntity.status(404).body("Not found");
        }
        return ResponseEntity.ok(club);
    }



    // CREATE
    @PostMapping
    public ResponseEntity<?> create(@RequestBody Club club) {
        try {
            return ResponseEntity.ok(service.create(club));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id,
                                    @RequestBody Club club) {
        Club updated = service.update(id, club);
        if (updated == null) return ResponseEntity.status(404).body("Not found");
        return ResponseEntity.ok(updated);
    }

    // DELETE (soft delete)
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.ok("Deleted ID = " + id);
    }
}
