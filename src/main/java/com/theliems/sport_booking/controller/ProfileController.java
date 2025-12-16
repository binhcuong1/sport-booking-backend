package com.theliems.sport_booking.controller;

import com.theliems.sport_booking.model.Profile;
import com.theliems.sport_booking.service.ProfileService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {

    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    // GET ALL
    @GetMapping
    public List<Profile> getAll() {
        return profileService.getAll();
    }

    // GET BY ID
    @GetMapping("/{id}")
    public Profile getById(@PathVariable Integer id) {
        return profileService.getById(id);
    }

    // GET BY ACCOUNT
    @GetMapping("/account/{accountId}")
    public Profile getByAccount(@PathVariable Integer accountId) {
        return profileService.getByAccountId(accountId);
    }

    // CREATE
    @PostMapping("/account/{accountId}")
    public Profile create(
            @PathVariable Integer accountId,
            @RequestBody Profile profile
    ) {
        return profileService.create(accountId, profile);
    }

    // UPDATE
    @PutMapping("/{id}")
    public Profile update(
            @PathVariable Integer id,
            @RequestBody Profile profile
    ) {
        return profileService.update(id, profile);
    }

    // DELETE (SOFT)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        profileService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
