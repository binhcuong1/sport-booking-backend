package com.theliems.sport_booking.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Club_Service")
@IdClass(ClubServiceId.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClubService {

    @Id
    @Column(name = "club_id")
    private Integer clubId;

    @Id
    @Column(name = "service_type_id")
    private Integer serviceTypeId;

    @Column(name = "name", length = 255)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "is_deleted")
    private Boolean isDeleted = false;
}
