package com.theliems.sport_booking.repository;

import com.theliems.sport_booking.model.Club;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ClubRepository extends JpaRepository<Club, Integer> {

    // Lấy danh sách club + sportTypes (dùng cho map)
    @EntityGraph(attributePaths = {"sportTypes"})
    List<Club> findByIsDeletedFalse();

    // Lấy 1 club + sportTypes
    @EntityGraph(attributePaths = {"sportTypes"})
    Optional<Club> findByClubIdAndIsDeletedFalse(Integer clubId);
}
