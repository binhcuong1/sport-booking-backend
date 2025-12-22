package com.theliems.sport_booking.repository;

import com.theliems.sport_booking.model.Court;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CourtRepository extends JpaRepository<Court, Integer> {

    @Query(value = """
                SELECT COUNT(*) FROM court
                WHERE club_id = :clubId
                AND sport_type_id = :sportTypeId
                AND LOWER(court_name) = LOWER(:name)
                AND is_deleted = 0
            """, nativeQuery = true)
    int existsCourtName(int clubId, int sportTypeId, String name);

}
