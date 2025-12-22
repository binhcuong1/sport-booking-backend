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
@IdClass(ClubSportTypeId.class)
public class ClubSportType {

    @Id
    @Column(name = "club_id")
    private Integer clubId;

    @Id
    @Column(name = "sport_type_id")
    private Integer sportTypeId;
}
