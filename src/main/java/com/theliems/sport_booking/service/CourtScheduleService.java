package com.theliems.sport_booking.service;

import com.theliems.sport_booking.model.Club;
import com.theliems.sport_booking.model.Court;
import com.theliems.sport_booking.model.CourtScheduleStatus;
import com.theliems.sport_booking.model.ScheduleSlot;
import com.theliems.sport_booking.repository.ClubRepository;
import com.theliems.sport_booking.repository.CourtRepository;
import com.theliems.sport_booking.repository.ScheduleSlotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourtScheduleService {

    private final ScheduleSlotRepository slotRepo;
    private final CourtRepository courtRepo; // must provide findByClubAndSportType
    private final ClubRepository clubRepo;

    // 1) Build daily schedule response
    public Map<String, Object> buildDailySchedule(Integer clubId, Integer sportTypeId, LocalDate date) {
        List<Court> courts = courtRepo.findByClubAndSportType(clubId, sportTypeId);
        Map<String, Object> out = new HashMap<>();
        out.put("date", date.toString());

        List<Map<String, Object>> courtsOut = new ArrayList<>();
        for (Court court : courts) {
            Map<String, Object> c = new HashMap<>();
            c.put("courtId", court.getCourtId());
            c.put("courtName", court.getCourtName());

            List<ScheduleSlot> slots = slotRepo.findByCourtIdAndDateOrderByStartTime(court.getCourtId(), date);
            List<Map<String, Object>> sOut = slots.stream().map(sl -> {
                Map<String, Object> m = new HashMap<>();
                m.put("startTime", sl.getStartTime().toString());
                m.put("endTime", sl.getEndTime().toString());
                // map DB status -> FE label (you can keep same words)
                m.put("status", sl.getStatus()); // "available" | "booked" | "blocked"
                m.put("price", sl.getPrice());
                return m;
            }).collect(Collectors.toList());

            c.put("schedules", sOut);
            courtsOut.add(c);
        }

        out.put("courts", courtsOut);
        return out;
    }

    // 2) Update single slot (create if not exist)
    public boolean updateSingleSlot(Integer clubId, Integer courtId, LocalDate date,
                                    LocalTime startTime, LocalTime endTime,
                                    String status, Double price) {
        Optional<ScheduleSlot> opt = slotRepo.findByClubIdAndCourtIdAndDateAndStartTime(
                clubId, courtId, date, startTime);

        if (opt.isPresent()) {
            ScheduleSlot s = opt.get();
            if ("booked".equalsIgnoreCase(String.valueOf(s.getStatus()))) return false; // cannot change booked
            s.setStatus(CourtScheduleStatus.valueOf(status));
            s.setPrice(price);
            s.setEndTime(endTime);
            slotRepo.save(s);
            return true;
        } else {
            // create new slot (ok unless booking const raints exist)
            ScheduleSlot s = new ScheduleSlot();
            s.setClubId(clubId);
            s.setCourtId(courtId);
            s.setDate(date);
            s.setStartTime(startTime);
            s.setEndTime(endTime);
            s.setStatus(CourtScheduleStatus.valueOf(status));
            s.setPrice(price);
            slotRepo.save(s);
            return true;
        }
    }

    // 3) Bulk update
    public int updateBulkSlots(Integer clubId, Integer courtId, LocalDate date,
                               List<Map<String, String>> slots, String status, Double price) {
        int updated = 0;
        for (Map<String, String> sl : slots) {
            LocalTime st = LocalTime.parse(sl.get("startTime"));
            LocalTime et = LocalTime.parse(sl.get("endTime"));
            Optional<ScheduleSlot> opt = slotRepo.findByClubIdAndCourtIdAndDateAndStartTime(
                    clubId, courtId, date, st);
            if (opt.isPresent()) {
                ScheduleSlot ex = opt.get();
                if ("booked".equalsIgnoreCase(String.valueOf(ex.getStatus()))) continue;
                ex.setStatus(CourtScheduleStatus.valueOf(status));
                ex.setPrice(price);
                ex.setEndTime(et);
                slotRepo.save(ex);
                updated++;
            } else {
                ScheduleSlot s = new ScheduleSlot(null, clubId, courtId, date, st, et, status, price);
                slotRepo.save(s);
                updated++;
            }
        }
        return updated;
    }

    // 4) Generate slots for date (do not override booked). stepMinutes from step string e.g. "30m"
    public int generateSlots(Integer clubId, Integer sportTypeId, LocalDate date,
                             List<Integer> courtIds, LocalTime startTime, LocalTime endTime,
                             int stepMinutes, Double defaultPrice, boolean overrideEmpty) {

        int created = 0;
        // ensure these courts belong to club & sportType (optional check)
        List<Court> courts = courtRepo.findByClubAndSportType(clubId, sportTypeId);
        Set<Integer> allowed = courts.stream().map(Court::getCourtId).collect(Collectors.toSet());

        for (Integer courtId : courtIds) {
            if (!allowed.contains(courtId)) continue;
            LocalTime t = startTime;
            while (t.isBefore(endTime)) {
                LocalTime tEnd = t.plusMinutes(stepMinutes);
                Optional<ScheduleSlot> opt = slotRepo.findByClubIdAndCourtIdAndDateAndStartTime(
                        clubId, courtId, date, t);
                if (opt.isPresent()) {
                    ScheduleSlot ex = opt.get();
                    if ("booked".equalsIgnoreCase(String.valueOf(ex.getStatus()))) {
                        // skip
                    } else if ("available".equalsIgnoreCase(String.valueOf(ex.getStatus())) && overrideEmpty) {
                        ex.setPrice(defaultPrice);
                        slotRepo.save(ex);
                    } // if blocked and we don't want override -> skip
                } else {
                    ScheduleSlot s = new ScheduleSlot(null, clubId, courtId, date, t, tEnd, "available", defaultPrice);
                    slotRepo.save(s);
                    created++;
                }
                t = tEnd;
            }
        }
        return created;
    }

    // 5) Lock day (mark existing non-booked slots to blocked)
    public boolean lockDay(Integer clubId, Integer sportTypeId, LocalDate date) {
        List<Court> courts = courtRepo.findByClubAndSportType(clubId, sportTypeId);
        boolean anyChanged = false;
        for (Court c : courts) {
            List<ScheduleSlot> slots = slotRepo.findByCourtIdAndDateOrderByStartTime(c.getCourtId(), date);
            for (ScheduleSlot s : slots) {
                if (!"booked".equalsIgnoreCase(String.valueOf(s.getStatus())) && !"blocked".equalsIgnoreCase(String.valueOf(s.getStatus()))) {
                    s.setStatus(CourtScheduleStatus.valueOf("blocked"));
                    slotRepo.save(s);
                    anyChanged = true;
                }
            }
        }
        return anyChanged;
    }
}

