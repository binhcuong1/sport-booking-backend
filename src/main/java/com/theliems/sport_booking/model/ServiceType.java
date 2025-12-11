package com.theliems.sport_booking.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Service_Type")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ServiceType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer service_type_id;

    private String type_name;
}
