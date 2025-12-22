package com.theliems.sport_booking.repository;

import com.theliems.sport_booking.model.Club;
import com.theliems.sport_booking.model.SportType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClubRepository extends JpaRepository<Club, Integer> {

    List<Club> findByIsDeletedFalse();

    Club findByClubIdAndIsDeletedFalse(Integer id);

    boolean existsByClubNameIgnoreCase(String clubName);

    @Query(value = """
            SELECT DISTINCT st.*
            FROM court c
            JOIN sport_type st ON c.sport_type_id = st.sport_type_id
            WHERE c.club_id = :clubId
            AND c.is_deleted = 0
            """, nativeQuery = true)
    List<SportType> findSportTypesByClubId(@Param("clubId") int clubId);


}