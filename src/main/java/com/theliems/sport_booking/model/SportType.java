package com.theliems.sport_booking.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Sport_Type")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SportType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sport_type_id")
    private Integer sport_type_id;

    @Column(name = "sport_name", nullable = false, length = 100)
    private String sport_name;

    @Column(name = "is_deleted")
    private Boolean is_deleted = false;
}