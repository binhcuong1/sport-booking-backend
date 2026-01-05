package com.theliems.sport_booking.controller;

import com.theliems.sport_booking.service.StatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/stats")
@RequiredArgsConstructor
public class StatsController {

    private final StatsService statsService;

    @GetMapping("/overview")
    public ResponseEntity<?> overview(
            @RequestParam LocalDate fromDate,
            @RequestParam LocalDate toDate,
            @RequestParam String clubId,
            Authentication auth
    ) {
        return ResponseEntity.ok(
                statsService.overview(auth, fromDate, toDate, clubId)
        );
    }

    @GetMapping("/revenue")
    public ResponseEntity<?> revenue(
            @RequestParam LocalDate fromDate,
            @RequestParam LocalDate toDate,
            @RequestParam String groupBy,
            @RequestParam String clubId,
            Authentication auth
    ) {
        return ResponseEntity.ok(
                statsService.revenue(auth, fromDate, toDate, groupBy, clubId)
        );
    }

    @GetMapping("/by-club")
    public ResponseEntity<?> byClub(
            @RequestParam LocalDate fromDate,
            @RequestParam LocalDate toDate,
            Authentication auth
    ) {
        return ResponseEntity.ok(
                statsService.byClub(auth, fromDate, toDate)
        );
    }

    @GetMapping("/occupancy")
    public ResponseEntity<?> occupancy(
            @RequestParam LocalDate fromDate,
            @RequestParam LocalDate toDate,
            @RequestParam String clubId,
            Authentication auth
    ) {
        return ResponseEntity.ok(
                statsService.occupancy(auth, fromDate, toDate, clubId)
        );
    }

    @GetMapping("/hot-hours")
    public ResponseEntity<?> hotHours(
            @RequestParam LocalDate fromDate,
            @RequestParam LocalDate toDate,
            @RequestParam String clubId,
            Authentication auth
    ) {
        return ResponseEntity.ok(
                statsService.hotHours(auth, fromDate, toDate, clubId)
        );
    }

    @GetMapping("/top-customers")
    public ResponseEntity<?> topCustomers(
            @RequestParam LocalDate fromDate,
            @RequestParam LocalDate toDate,
            @RequestParam(defaultValue = "10") Integer limit,
            @RequestParam String clubId,
            Authentication auth
    ) {
        return ResponseEntity.ok(
                statsService.topCustomers(auth, fromDate, toDate, limit, clubId)
        );
    }
}
