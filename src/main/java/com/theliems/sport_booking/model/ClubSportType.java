package com.theliems.sport_booking.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "club_sport_type")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClubSportType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "club_id", nullable = false)
    private Integer clubId;

    @Column(name = "sport_type_id", nullable = false)
    private Integer sportTypeId;
}