package com.theliems.sport_booking.controller;

import com.theliems.sport_booking.service.CourtScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/court-schedules")
@RequiredArgsConstructor
public class CourtScheduleController {

    private final CourtScheduleService service;

    // GET /admin/court-schedules?clubId=1&sportTypeId=2&date=2025-12-20
    @GetMapping
    public ResponseEntity<?> getDaily(@RequestParam Integer clubId,
                                      @RequestParam Integer sportTypeId,
                                      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(service.buildDailySchedule(clubId, sportTypeId, date));
    }

    // PUT /admin/court-schedules  (single)
    @PutMapping
    public ResponseEntity<?> updateSingle(@RequestBody Map<String, Object> req) {
        Integer clubId = (Integer) req.get("clubId");
        Integer courtId = (Integer) req.get("courtId");
        LocalDate date = LocalDate.parse((String) req.get("date"));
        LocalTime st = LocalTime.parse((String) req.get("startTime"));
        LocalTime et = LocalTime.parse((String) req.get("endTime"));
        String status = (String) req.get("status"); // expected "available"/"booked"/"blocked"
        Double price = req.get("price") == null ? null : ((Number) req.get("price")).doubleValue();

        boolean ok = service.updateSingleSlot(clubId, courtId, date, st, et, status, price);
        if (!ok) return ResponseEntity.badRequest().body(Map.of("success", false, "reason", "slot is booked"));
        return ResponseEntity.ok(Map.of("success", true));
    }

    // PUT /admin/court-schedules/bulk
    @PutMapping("/bulk")
    public ResponseEntity<?> updateBulk(@RequestBody Map<String, Object> req) {
        Integer clubId = (Integer) req.get("clubId");
        Integer courtId = (Integer) req.get("courtId");
        LocalDate date = LocalDate.parse((String) req.get("date"));
        List<Map<String, String>> slots = (List<Map<String, String>>) req.get("slots");
        String status = (String) req.get("status");
        Double price = req.get("price") == null ? null : ((Number) req.get("price")).doubleValue();

        int updated = service.updateBulkSlots(clubId, courtId, date, slots, status, price);
        return ResponseEntity.ok(Map.of("updatedCount", updated));
    }

    // POST /admin/court-schedules/generate
    @PostMapping("/generate")
    public ResponseEntity<?> generate(@RequestBody Map<String, Object> req) {
        Integer clubId = (Integer) req.get("clubId");
        Integer sportTypeId = (Integer) req.get("sportTypeId");
        LocalDate date = LocalDate.parse((String) req.get("date"));
        List<Integer> courtIds = (List<Integer>) req.get("courtIds");
        LocalTime start = LocalTime.parse((String) req.get("startTime"));
        LocalTime end = LocalTime.parse((String) req.get("endTime"));
        String step = (String) req.get("step"); // e.g. "30m"
        int stepMinutes = Integer.parseInt(step.replaceAll("[^0-9]", ""));
        Double defaultPrice = ((Number) req.get("defaultPrice")).doubleValue();
        boolean overrideEmpty = req.get("overrideEmpty") == null ? false : (Boolean) req.get("overrideEmpty");

        int created = service.generateSlots(clubId, sportTypeId, date, courtIds, start, end, stepMinutes, defaultPrice, overrideEmpty);
        return ResponseEntity.ok(Map.of("createdCount", created));
    }

    // PUT /admin/court-schedules/lock-day
    @PutMapping("/lock-day")
    public ResponseEntity<?> lockDay(@RequestBody Map<String, Object> req) {
        Integer clubId = (Integer) req.get("clubId");
        Integer sportTypeId = (Integer) req.get("sportTypeId");
        LocalDate date = LocalDate.parse((String) req.get("date"));
        boolean locked = service.lockDay(clubId, sportTypeId, date);
        return ResponseEntity.ok(Map.of("locked", locked));
    }
}

