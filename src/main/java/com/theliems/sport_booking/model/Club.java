package com.theliems.sport_booking.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalTime;

@Entity
@Table(name = "Club")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Club {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer club_id;

    private String club_name;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String address;

    private LocalTime open_time;

    private LocalTime close_time;

    private String contact_phone;

    private Boolean is_deleted = false;
}
