package com.theliems.sport_booking.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "court_schedule")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleSlot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "court_schedule_id")
    private Integer courtScheduleId;

    @Column(name = "court_id")
    private Integer courtId;

    @Column(name = "club_id", nullable = false)
    private Integer clubId;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "start_time")
    private LocalTime startTime;

    @Column(name = "end_time")
    private LocalTime endTime;

    // ENUM('available', 'booked', 'blocked')
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private CourtScheduleStatus status;

    @Column(name = "price")
    private Double price;

    public ScheduleSlot(
            Integer courtScheduleId,
            Integer clubId,
            Integer courtId,
            LocalDate date,
            LocalTime st,
            LocalTime et,
            String status,
            Double price) {
        this.clubId = clubId;
        this.courtId = courtId;
        this.date = date;
        this.startTime = st;
        this.endTime = et;
        this.status = CourtScheduleStatus.valueOf(status);
        this.price = price;

    }
}