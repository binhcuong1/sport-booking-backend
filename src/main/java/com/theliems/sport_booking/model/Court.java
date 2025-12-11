package com.theliems.sport_booking.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Court")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Court {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer court_id;

    private String court_name;

    private Boolean is_deleted = false;

    @ManyToOne
    @JoinColumn(name = "sport_type_id")
    private SportType sportType;
}
