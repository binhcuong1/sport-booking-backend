package com.theliems.sport_booking.repository;

import com.theliems.sport_booking.model.Booking;
import com.theliems.sport_booking.model.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Integer> {

    /* =================================================
       CHECK USER HAS BOOKING WITH STATUS
    ================================================= */

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

    /* =================================================
       USER – BOOKING HISTORY
    ================================================= */

    @Query("""
        SELECT
            b.bookingId,
            c.clubName,
            p.fullname,
            b.totalTime,
            b.totalPrice,
            b.paymentMethod,
            b.note,
            b.bookingStatus,
            b.createdAt
        FROM Booking b
        JOIN Club c ON b.clubId = c.clubId
        JOIN Profile p ON b.profileId = p.id
        WHERE b.profileId = :profileId
        ORDER BY b.createdAt DESC
    """)
    List<Object[]> findUserBookingsWithClubName(
            @Param("profileId") Integer profileId
    );

    /* =================================================
       BOOKING DETAIL
    ================================================= */

    @Query("""
        SELECT
            b.bookingId,
            c.clubName,
            b.totalTime,
            b.totalPrice,
            b.paymentMethod,
            b.note,
            b.bookingStatus,
            b.createdAt
        FROM Booking b
        JOIN Club c ON b.clubId = c.clubId
        WHERE b.bookingId = :bookingId
    """)
    Object[] findBookingDetailWithClubName(
            @Param("bookingId") Integer bookingId
    );

    /* =================================================
       ADMIN – BOOKINGS BY CLUB
    ================================================= */

    @Query("""
        SELECT
            b.bookingId,
            c.clubName,
            p.fullname,
            b.profileId,
            b.totalTime,
            b.bookingStatus,
            b.createdAt
        FROM Booking b
        JOIN Club c ON b.clubId = c.clubId
        JOIN Profile p ON b.profileId = p.id
        WHERE b.clubId = :clubId
        ORDER BY b.createdAt DESC
    """)
    List<Object[]> findBookingsWithClubName(
            @Param("clubId") Integer clubId
    );

    /* =================================================
       ADMIN – ALL BOOKINGS
    ================================================= */

    @Query("""
        SELECT
            b.bookingId,
            c.clubName,
            p.fullname,
            b.profileId,
            b.totalTime,
            b.bookingStatus,
            b.createdAt
        FROM Booking b
        JOIN Club c ON b.clubId = c.clubId
        JOIN Profile p ON b.profileId = p.id
        ORDER BY b.createdAt DESC
    """)
    List<Object[]> findAllBookingsWithClubName();
}
