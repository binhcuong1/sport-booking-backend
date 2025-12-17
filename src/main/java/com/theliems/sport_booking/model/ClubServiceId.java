package com.theliems.sport_booking.model;

import java.io.Serializable;
import java.util.Objects;

public class ClubServiceId implements Serializable {
    private Integer clubId;
    private Integer serviceTypeId;

    public ClubServiceId() {}

    public ClubServiceId(Integer clubId, Integer serviceTypeId) {
        this.clubId = clubId;
        this.serviceTypeId = serviceTypeId;
    }

    public Integer getClubId() { return clubId; }
    public void setClubId(Integer clubId) { this.clubId = clubId; }

    public Integer getServiceTypeId() { return serviceTypeId; }
    public void setServiceTypeId(Integer serviceTypeId) { this.serviceTypeId = serviceTypeId; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ClubServiceId that)) return false;
        return Objects.equals(clubId, that.clubId)
                && Objects.equals(serviceTypeId, that.serviceTypeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(clubId, serviceTypeId);
    }
}
