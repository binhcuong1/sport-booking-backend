package com.theliems.sport_booking.repository;

import com.theliems.sport_booking.model.Club;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClubRepository extends JpaRepository<Club, Integer> {

    List<Club> findByIsDeletedFalse();

    Club findByClubIdAndIsDeletedFalse(Integer id);

    boolean existsByClubNameIgnoreCase(String clubName);
}