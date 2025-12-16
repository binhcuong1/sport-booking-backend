package com.theliems.sport_booking.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Profile")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer profile_id;

    private String fullname;

    @Enumerated(EnumType.STRING)
    private Gender gender = Gender.unknown;

    public enum Gender {
        nam, nu, unknown
    }

    private String avatar_url;

    private Boolean is_deleted = false;

    @OneToOne
    @JoinColumn(name = "account_id", unique = true)
    private Account account;
}
