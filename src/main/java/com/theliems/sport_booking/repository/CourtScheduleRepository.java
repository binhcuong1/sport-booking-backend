package com.theliems.sport_booking.repository;

import com.theliems.sport_booking.model.CourtSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourtScheduleRepository
        extends JpaRepository<CourtSchedule, Integer> {

    List<CourtSchedule> findByCourt_ClubIdAndStatusNot(
            Integer clubId,
            CourtSchedule.Status status
    );
}
