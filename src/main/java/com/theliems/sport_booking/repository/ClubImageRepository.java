package com.theliems.sport_booking.repository;

import com.theliems.sport_booking.model.ClubImage;
import com.theliems.sport_booking.model.ClubImageId;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ClubImageRepository extends JpaRepository<ClubImage, ClubImageId> {

    List<ClubImage> findByIdClubId(Integer clubId);
}
