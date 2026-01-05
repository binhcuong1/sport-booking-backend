package com.theliems.sport_booking.service;

import com.theliems.sport_booking.model.Account;
import com.theliems.sport_booking.model.AccountClub;
import com.theliems.sport_booking.repository.AccountClubRepository;
import com.theliems.sport_booking.repository.AccountRepository;
import com.theliems.sport_booking.repository.StatsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class StatsService {

    private final StatsRepository statsRepo;
    private final AccountClubRepository accountClubRepo;
    private final AccountRepository accountRepo;

    /* ================== UTILS ================== */

    private List<Integer> resolveClubIds(Authentication auth, String clubIdParam) {
        String email = auth.getName();
        Account account = accountRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Account không tồn tại"));

        Integer accountId = account.getAccountId();

        List<Integer> ownerClubIds = accountClubRepo
                .findByAccountIdAndIsDeletedFalse(accountId)
                .stream()
                .map(AccountClub::getClubId)
                .toList();

        if (ownerClubIds.isEmpty()) {
            throw new RuntimeException("Owner chưa có CLB nào");
        }

        if ("all".equalsIgnoreCase(clubIdParam)) {
            return ownerClubIds;
        }

        Integer clubId = Integer.parseInt(clubIdParam);
        if (!ownerClubIds.contains(clubId)) {
            throw new RuntimeException("Không có quyền truy cập CLB này");
        }

        return List.of(clubId);
    }

    /* ================== API 1 ================== */

    public Map<String, Object> overview(
            Authentication auth,
            LocalDate from,
            LocalDate to,
            String clubId
    ) {
        List<Integer> clubIds = resolveClubIds(auth, clubId);

        return Map.of(
                "totalBooking", statsRepo.totalBooking(clubIds, from, to),
                "totalRevenue", statsRepo.totalRevenue(clubIds, from, to),
                "totalHours", statsRepo.totalHours(clubIds, from, to),
                "totalCustomers", statsRepo.totalCustomers(clubIds, from, to),
                "todayBooking", statsRepo.todayBooking(clubIds, LocalDate.now())
        );
    }

    /* ================== API 2 ================== */

    public Map<String, Object> revenue(
            Authentication auth,
            LocalDate from,
            LocalDate to,
            String groupBy,
            String clubId
    ) {
        List<Integer> clubIds = resolveClubIds(auth, clubId);

        List<Object[]> rows =
                "month".equals(groupBy)
                        ? statsRepo.revenueByMonth(clubIds, from, to)
                        : statsRepo.revenueByDay(clubIds, from, to);

        return Map.of(
                "labels", rows.stream().map(r -> r[0]).toList(),
                "data", rows.stream().map(r -> r[1]).toList()
        );
    }

    /* ================== API 3 ================== */

    public Map<String, Object> byClub(
            Authentication auth,
            LocalDate from,
            LocalDate to
    ) {
        String email = auth.getName();
        Account account = accountRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Account không tồn tại"));

        Integer accountId = account.getAccountId();
        return Map.of(
                "clubs", statsRepo.bookingByClub(accountId, from, to)
        );
    }

    /* ================== API 4 ================== */

    public Map<String, Object> occupancy(
            Authentication auth,
            LocalDate from,
            LocalDate to,
            String clubId
    ) {
        List<Integer> clubIds = resolveClubIds(auth, clubId);
        return Map.of(
                "clubs", statsRepo.occupancy(clubIds, from, to)
        );
    }

    /* ================== API 5 ================== */

    public Map<String, Object> hotHours(
            Authentication auth,
            LocalDate from,
            LocalDate to,
            String clubId
    ) {
        List<Integer> clubIds = resolveClubIds(auth, clubId);
        return Map.of(
                "hours", statsRepo.hotHours(clubIds, from, to)
        );
    }

    /* ================== API 6 ================== */

    public Map<String, Object> topCustomers(
            Authentication auth,
            LocalDate from,
            LocalDate to,
            Integer limit,
            String clubId
    ) {
        List<Integer> clubIds = resolveClubIds(auth, clubId);
        return Map.of(
                "customers", statsRepo.topCustomers(clubIds, from, to, limit)
        );
    }
}
