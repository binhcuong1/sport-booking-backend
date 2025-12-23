package com.theliems.sport_booking.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "court")
public class Court {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "court_id")
    private Integer courtId;

    @Column(name = "sport_type_id")
    private Integer sportTypeId;

    @Column(name = "court_name")
    private String courtName;

    @Column(name = "is_deleted")
    private Boolean isDeleted = false;

    @Column(name = "club_id", nullable = false)
    private Integer clubId;
}

