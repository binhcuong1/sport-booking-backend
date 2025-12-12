package com.theliems.sport_booking.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "Court_Schedule")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CourtSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer court_schedule_id;

    @ManyToOne
    @JoinColumn(name = "court_id")
    private Court court;

    private LocalTime start_time;
    private LocalTime end_time;

    private LocalDate date;

    @Enumerated(EnumType.STRING)
    private Status status = Status.available;

    public enum Status {
        available, booked, blocked
        }

    private Double price;
}
