package com.theliems.sport_booking.repository;

import com.theliems.sport_booking.model.ClubImage;
import com.theliems.sport_booking.model.ClubImageId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ClubImageRepository
        extends JpaRepository<ClubImage, ClubImageId> {
    List<ClubImage> findByIdClubId(Integer clubId);
    @Query(value = """
        SELECT ci.image_url
        FROM club_image ci
        JOIN image_type it ON it.image_type_id = ci.image_type_id
        WHERE ci.club_id = :clubId
          AND it.type_name = 'cover'
          AND ci.is_deleted = 0
        LIMIT 1
    """, nativeQuery = true)
    String findCoverByClub(@Param("clubId") Integer clubId);
}
