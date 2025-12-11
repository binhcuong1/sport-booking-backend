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
    private Integer sport_type_id;

    private String sport_name;

    private Boolean is_deleted = false;
}
