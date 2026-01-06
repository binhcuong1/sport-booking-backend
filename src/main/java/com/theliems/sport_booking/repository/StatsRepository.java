package com.theliems.sport_booking.repository;

import com.theliems.sport_booking.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Repository
public interface StatsRepository extends JpaRepository<Booking, Integer> {

    /* ===== OVERVIEW ===== */

    @Query(value = """
        SELECT COUNT(*)
        FROM booking
        WHERE club_id IN :clubIds
          AND booking_status = 'hoàn thành'
          AND DATE(created_at) BETWEEN :from AND :to
    """, nativeQuery = true)
    Integer totalBooking(List<Integer> clubIds, LocalDate from, LocalDate to);

    @Query(value = """
        SELECT COALESCE(SUM(total_price),0)
        FROM booking
        WHERE club_id IN :clubIds
          AND booking_status = 'hoàn thành'
          AND DATE(created_at) BETWEEN :from AND :to
    """, nativeQuery = true)
    Double totalRevenue(List<Integer> clubIds, LocalDate from, LocalDate to);

    @Query(value = """
        SELECT COALESCE(SUM(total_time),0)
        FROM booking
        WHERE club_id IN :clubIds
          AND booking_status = 'hoàn thành'
          AND DATE(created_at) BETWEEN :from AND :to
    """, nativeQuery = true)
    Integer totalHours(List<Integer> clubIds, LocalDate from, LocalDate to);

    @Query(value = """
        SELECT COUNT(DISTINCT profile_id)
        FROM booking
        WHERE club_id IN :clubIds
          AND booking_status = 'hoàn thành'
          AND DATE(created_at) BETWEEN :from AND :to
    """, nativeQuery = true)
    Integer totalCustomers(List<Integer> clubIds, LocalDate from, LocalDate to);

    @Query(value = """
        SELECT COUNT(*)
        FROM booking
        WHERE club_id IN :clubIds
          AND DATE(created_at) = :today
    """, nativeQuery = true)
    Integer todayBooking(List<Integer> clubIds, LocalDate today);

    /* ===== REVENUE ===== */

    @Query(value = """
        SELECT DATE(created_at) as d, SUM(total_price)
        FROM booking
        WHERE club_id IN :clubIds
          AND booking_status = 'hoàn thành'
          AND DATE(created_at) BETWEEN :from AND :to
        GROUP BY d
        ORDER BY d
    """, nativeQuery = true)
    List<Object[]> revenueByDay(List<Integer> clubIds, LocalDate from, LocalDate to);

    @Query(value = """
        SELECT DATE_FORMAT(created_at,'%Y-%m') as m, SUM(total_price)
        FROM booking
        WHERE club_id IN :clubIds
          AND booking_status = 'hoàn thành'
          AND DATE(created_at) BETWEEN :from AND :to
        GROUP BY m
        ORDER BY m
    """, nativeQuery = true)
    List<Object[]> revenueByMonth(List<Integer> clubIds, LocalDate from, LocalDate to);

    /* ===== BY CLUB ===== */

    @Query(value = """
        SELECT c.club_id, c.club_name,
               COUNT(b.booking_id),
               SUM(b.total_price)
        FROM club c
        JOIN booking b ON b.club_id = c.club_id
        WHERE b.booking_status = 'hoàn thành'
          AND DATE(b.created_at) BETWEEN :from AND :to
          AND c.club_id IN (
              SELECT club_id FROM account_club WHERE account_id = :accountId
          )
        GROUP BY c.club_id
    """, nativeQuery = true)
    List<Map<String, Object>> bookingByClub(Integer accountId, LocalDate from, LocalDate to);

    /* ===== OCCUPANCY ===== */

    @Query(value = """
        SELECT c.club_id, c.club_name,
               COUNT(cs.court_schedule_id) AS totalSlots,
               SUM(cs.status = 'booked') AS bookedSlots,
               SUM(cs.status = 'booked') / COUNT(cs.court_schedule_id) AS occupancyRate
        FROM court_schedule cs
        JOIN court ct ON cs.court_id = ct.court_id
        JOIN club c ON ct.club_id = c.club_id
        WHERE c.club_id IN :clubIds
          AND cs.date BETWEEN :from AND :to
        GROUP BY c.club_id
    """, nativeQuery = true)
    List<Map<String, Object>> occupancy(List<Integer> clubIds, LocalDate from, LocalDate to);

    /* ===== HOT HOURS ===== */

    @Query(value = """
        SELECT HOUR(start_time) as hour, COUNT(*)
        FROM court_schedule cs
        JOIN court c ON cs.court_id = c.court_id
        WHERE c.club_id IN :clubIds
          AND cs.status = 'booked'
          AND cs.date BETWEEN :from AND :to
        GROUP BY hour
        ORDER BY hour
    """, nativeQuery = true)
    List<Map<String, Object>> hotHours(List<Integer> clubIds, LocalDate from, LocalDate to);

    /* ===== TOP CUSTOMERS ===== */

    @Query(value = """
        SELECT p.profile_id, p.fullname,
               COUNT(b.booking_id) AS totalBooking,
               SUM(b.total_price) AS totalSpent
        FROM booking b
        JOIN profile p ON b.profile_id = p.profile_id
        WHERE b.club_id IN :clubIds
          AND b.booking_status = 'hoàn thành'
          AND DATE(b.created_at) BETWEEN :from AND :to
        GROUP BY p.profile_id
        ORDER BY totalSpent DESC
        LIMIT :limit
    """, nativeQuery = true)
    List<Map<String, Object>> topCustomers(
            List<Integer> clubIds,
            LocalDate from,
            LocalDate to,
            Integer limit
    );
}
