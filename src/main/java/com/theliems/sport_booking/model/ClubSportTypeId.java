package com.theliems.sport_booking.model;

import java.io.Serializable;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClubSportTypeId implements Serializable {
    private Integer clubId;
    private Integer sportTypeId;
}
