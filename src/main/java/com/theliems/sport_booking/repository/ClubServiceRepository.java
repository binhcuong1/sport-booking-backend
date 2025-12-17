package com.theliems.sport_booking.repository;

import com.theliems.sport_booking.model.ClubService;
import com.theliems.sport_booking.model.ClubServiceId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ClubServiceRepository extends JpaRepository<ClubService, ClubServiceId> {

    interface ClubServiceRow {
        Integer getClubId();
        String getClubName();

        Integer getServiceTypeId();
        String getServiceTypeName();

        String getName();
        String getDescription();

        Boolean getIsDeleted();
    }

    @Query(value = """
        SELECT
            cs.club_id          AS clubId,
            c.club_name         AS clubName,
            cs.service_type_id  AS serviceTypeId,
            st.type_name        AS serviceTypeName,
            cs.name             AS name,
            cs.description      AS description,
            cs.is_deleted       AS isDeleted
        FROM club_service cs
        JOIN club c ON c.club_id = cs.club_id
        JOIN service_type st ON st.service_type_id = cs.service_type_id
        WHERE cs.club_id = :clubId
        ORDER BY st.type_name, cs.name
        """, nativeQuery = true)
    List<ClubServiceRow> findRowsByClub(@Param("clubId") Integer clubId);
}
