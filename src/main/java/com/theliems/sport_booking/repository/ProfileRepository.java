package com.theliems.sport_booking.repository;

import com.theliems.sport_booking.model.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProfileRepository extends JpaRepository<Profile, Integer> {

    Optional<Profile> findByAccount_AccountId(Integer accountId);
}
