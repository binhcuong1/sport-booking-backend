package com.theliems.sport_booking.repository;

import com.theliems.sport_booking.model.Booking;
import com.theliems.sport_booking.model.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BookingRepository extends JpaRepository<Booking, Integer> {

    @Query("""
        SELECT (COUNT(b) > 0)
        FROM Booking b
        WHERE b.profileId = :profileId
          AND b.clubId = :clubId
          AND b.bookingStatus = :status
    """)
    boolean hasCompletedBooking(
            @Param("profileId") Integer profileId,
            @Param("clubId") Integer clubId,
            @Param("status") BookingStatus status
    );
    java.util.List<Booking> findByProfileIdOrderByCreatedAtDesc(Integer profileId);
}

