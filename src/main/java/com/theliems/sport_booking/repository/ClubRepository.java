package com.theliems.sport_booking.repository;

import com.theliems.sport_booking.model.Club;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClubRepository extends JpaRepository<Club, Integer> {
}
