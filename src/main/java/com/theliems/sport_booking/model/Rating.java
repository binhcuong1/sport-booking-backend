package com.theliems.sport_booking.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Rating")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Rating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer rating_id;

    @ManyToOne
    @JoinColumn(name = "club_id")
    private Club club;

    @ManyToOne
    @JoinColumn(name = "profile_id")
    private Profile profile;

    private Integer score;

    @Column(columnDefinition = "TEXT")
    private String review;

    private LocalDateTime created_at = LocalDateTime.now();

    private Boolean is_deleted = false;
}
