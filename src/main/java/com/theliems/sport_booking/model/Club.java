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
    @Column(name = "club_id")
    private Integer clubId;

    @Column(name = "club_name")
    private String clubName;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String address;

    @Column(name = "open_time")
    private LocalTime openTime;

    @Column(name = "close_time")
    private LocalTime closeTime;

    @Column(name = "contact_phone")
    private String contactPhone;

    @Column(name = "is_deleted")
    private Boolean isDeleted = false;
}
