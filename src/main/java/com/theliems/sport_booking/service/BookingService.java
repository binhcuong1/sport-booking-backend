package com.theliems.sport_booking.service;

import com.theliems.sport_booking.model.*;
import com.theliems.sport_booking.repository.BookingCourtScheduleRepository;
import com.theliems.sport_booking.repository.BookingRepository;
import com.theliems.sport_booking.repository.ScheduleSlotRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepo;
    private final BookingCourtScheduleRepository bookingSlotRepo;
    private final ScheduleSlotRepository slotRepo;


    @Transactional
    public Integer createBooking(CreateBookingRequest req) {

        var slots = slotRepo.findAllById(
                req.getSelectedSlots()
                        .stream()
                        .map(SelectedSlot::getCourtScheduleId)
                        .toList()
        );

        for (var slot : slots) {
            if (slot.getStatus() != CourtScheduleStatus.available) {
                throw new RuntimeException("Slot đã được đặt");
            }
        }

        Booking booking = new Booking();
        booking.setClubId(req.getClubId());
        booking.setProfileId(req.getProfileId());
        booking.setPhoneNumber(req.getPhoneNumber());
        booking.setNote(req.getNote());
        booking.setPaymentMethod(PaymentMethod.vnpay);
        booking.setTotalTime(req.getTotalTime());
        booking.setTotalPrice(req.getTotalPrice());
        booking.setCreatedAt(LocalDateTime.now());
        booking.setBookingStatus(BookingStatus.DANG_XU_LY);

        bookingRepo.save(booking);

        for (var slot : slots) {
            BookingCourtSchedule bcs = new BookingCourtSchedule();
            bcs.setBookingId(booking.getBookingId());
            bcs.setCourtScheduleId(slot.getCourtScheduleId());
            bookingSlotRepo.save(bcs);

            slot.setStatus(CourtScheduleStatus.booked);
            slotRepo.save(slot);
        }

        return booking.getBookingId();
    }


    public List<Map<String, Object>> getBookingHistory(Integer profileId) {

        return bookingRepo.findUserBookingsWithClubName(profileId)
                .stream()
                .map(r -> {
                    Map<String, Object> m = new HashMap<>();
                    m.put("id", r[0]);
                    m.put("club", r[1]);
                    m.put("profileName", r[2]);
                    m.put("court", "Sân đã đặt");
                    m.put("time", r[3] + " giờ");
                    m.put("totalPrice", r[4]);
                    m.put("paymentMethod", r[5]);
                    m.put("note", r[6]);
                    m.put("status", mapStatus((BookingStatus) r[7]));
                    m.put("date", r[8].toString().substring(0, 10));
                    return m;
                })
                .toList();
    }


    public Map<String, Object> getBookingDetail(Integer bookingId) {

        Object result = bookingRepo.findBookingDetailWithClubName(bookingId);

        if (result == null) {
            throw new RuntimeException("Booking không tồn tại");
        }

        Object[] r;

        // FIX trường hợp Hibernate trả Object[1][]
        if (result instanceof Object[]) {
            Object[] tmp = (Object[]) result;
            if (tmp.length == 1 && tmp[0] instanceof Object[]) {
                r = (Object[]) tmp[0];
            } else {
                r = tmp;
            }
        } else {
            throw new RuntimeException("Dữ liệu booking không hợp lệ");
        }

        Map<String, Object> m = new HashMap<>();
        m.put("id", r[0]);
        m.put("club", r[1]);
        m.put("court", "Sân đã đặt");
        m.put("time", r[2] + " giờ");
        m.put("totalPrice", r[3]);
        m.put("paymentMethod", r[4]);
        m.put("note", r[5]);
        m.put("status", mapStatus((BookingStatus) r[6]));
        m.put("date", r[7].toString().substring(0, 10));
        m.put("createdAt", r[7].toString());

        return m;
    }



    public List<Map<String, Object>> getBookingsByClub(Integer clubId) {

        return bookingRepo.findBookingsWithClubName(clubId)
                .stream()
                .map(r -> {
                    Map<String, Object> m = new HashMap<>();
                    m.put("id", r[0]);
                    m.put("club", r[1]);
                    m.put("profileName", r[2]);
                    m.put("profileId", r[3]);
                    m.put("time", r[4] + " giờ");
                    m.put("totalPrice", r[5]);
                    m.put("status", mapStatus((BookingStatus) r[6]));
                    m.put("date", r[7].toString().substring(0, 10));
                    return m;
                })
                .toList();
    }



    public List<Map<String, Object>> getAllBookings() {

        return bookingRepo.findAllBookingsWithClubName()
                .stream()
                .map(r -> {
                    Map<String, Object> m = new HashMap<>();
                    m.put("id", r[0]);
                    m.put("club", r[1]);
                    m.put("profileName", r[2]);
                    m.put("profileId", r[3]);
                    m.put("time", r[4] + " giờ");
                    m.put("totalPrice", r[5]);
                    m.put("status", mapStatus((BookingStatus) r[6]));
                    m.put("date", r[7].toString().substring(0, 10));
                    return m;
                })
                .toList();
    }


    @Transactional
    public void updateStatus(Integer bookingId, BookingStatus status) {
        Booking b = bookingRepo.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking không tồn tại"));
        b.setBookingStatus(status);
        bookingRepo.save(b);
    }


    private String mapStatus(BookingStatus status) {
        return switch (status) {
            case DANG_XU_LY -> "pending";
            case HOAN_THANH -> "completed";
            case HUY -> "cancelled";
        };
    }
}
