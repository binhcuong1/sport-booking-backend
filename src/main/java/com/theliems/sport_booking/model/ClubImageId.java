package com.theliems.sport_booking.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Embeddable
public class ClubImageId implements Serializable {

    @Column(name = "club_id")
    private Integer clubId;

    @Column(name = "image_type_id")
    private Integer imageTypeId;
}
