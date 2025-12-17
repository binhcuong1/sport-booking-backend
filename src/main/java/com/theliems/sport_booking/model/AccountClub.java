package com.theliems.sport_booking.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(
        name = "account_club",
        uniqueConstraints = @UniqueConstraint(columnNames = {"account_id", "club_id"})
)
@Getter @Setter
public class AccountClub {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_club_id")
    private Integer accountClubId;

    @Column(name = "account_id")
    private Integer accountId;

    @Column(name = "club_id")
    private Integer clubId;

    @Column(name = "is_deleted")
    private Boolean isDeleted = false;
}

