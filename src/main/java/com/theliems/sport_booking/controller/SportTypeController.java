package com.theliems.sport_booking.controller;

import com.theliems.sport_booking.model.SportType;
import com.theliems.sport_booking.service.SportTypeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sport-types")
public class SportTypeController {

    private final SportTypeService service;

    public SportTypeController(SportTypeService service) {
        this.service = service;
    }

    // GET ALL
    @GetMapping
    public List<SportType> getAll() {
        return service.getAll();
    }

    // GET ONE
    @GetMapping("/{id}")
    public ResponseEntity<?> getOne(@PathVariable Integer id) {
        SportType st = service.getById(id);
        if (st == null) return ResponseEntity.status(404).body("Not found");
        return ResponseEntity.ok(st);
    }

    // CREATE
    @PostMapping
    public ResponseEntity<?> create(@RequestBody SportType sportType) {
        return ResponseEntity.ok(service.create(sportType));
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id, @RequestBody SportType sportType) {
        SportType updated = service.update(id, sportType);
        if (updated == null) return ResponseEntity.status(404).body("Not found");
        return ResponseEntity.ok(updated);
    }

    // DELETE (soft/hard tùy service.delete)
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.ok("Deleted ID = " + id);
    }
}
