package com.theliems.sport_booking.repository;

import com.theliems.sport_booking.model.ClubSportType;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ClubSportTypeRepository extends JpaRepository<ClubSportType, Long> {

    boolean existsByClubIdAndSportTypeId(int clubId, int sportTypeId);

    @Query(value = """
        SELECT COUNT(*) FROM court
        WHERE club_id = :clubId
        AND sport_type_id = :sportTypeId
        AND is_deleted = 0
    """, nativeQuery = true)
    int countCourtByClubAndSportType(int clubId, int sportTypeId);

    @Transactional
    void deleteByClubIdAndSportTypeId(
            Integer clubId,
            Integer sportTypeId
    );
}

