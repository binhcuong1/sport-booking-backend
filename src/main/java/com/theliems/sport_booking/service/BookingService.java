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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepo;
    private final BookingCourtScheduleRepository bookingSlotRepo;
    private final ScheduleSlotRepository slotRepo;

    @Transactional
    public Integer createBooking(CreateBookingRequest req) {

        // Check slot hợp lệ
        List<ScheduleSlot> slots = slotRepo.findAllById(
                req.getSelectedSlots()
                        .stream()
                        .map(SelectedSlot::getCourtScheduleId)
                        .toList()
        );

        for (ScheduleSlot slot : slots) {
            if (slot.getStatus() != CourtScheduleStatus.available) {
                throw new RuntimeException("Slot đã được đặt hoặc bị khóa");
            }
        }

        // Tạo booking
        Booking booking = new Booking();
        booking.setClubId(req.getClubId());
        booking.setProfileId(req.getProfileId());
        booking.setPhoneNumber(req.getPhoneNumber());
        booking.setNote(req.getNote());
        booking.setPaymentMethod(PaymentMethod.vnpay);
        booking.setTotalTime(req.getTotalTime());
        booking.setTotalPrice(req.getTotalPrice());
        booking.setCreatedAt(LocalDateTime.now());

        bookingRepo.save(booking);

        // Insert booking_court_schedule + update slot → booked
        for (ScheduleSlot slot : slots) {
            BookingCourtSchedule bcs = new BookingCourtSchedule();
            bcs.setBookingId(booking.getBookingId());
            bcs.setCourtScheduleId(slot.getCourtScheduleId());
            bookingSlotRepo.save(bcs);

            slot.setStatus(CourtScheduleStatus.valueOf("booked"));
            slotRepo.save(slot);
        }

        return booking.getBookingId();
    }
    public List<Map<String, Object>> getBookingHistory(Integer profileId) {

        List<Booking> bookings =
                bookingRepo.findByProfileIdOrderByCreatedAtDesc(profileId);

        return bookings.stream().map(b -> {
            Map<String, Object> m = new HashMap<>();

            m.put("id", b.getBookingId());
            m.put("club", "CLB #" + b.getClubId());   // FE chỉ cần text
            m.put("court", "Sân đã đặt");

            m.put("date", b.getCreatedAt()
                    .toLocalDate()
                    .toString());

            m.put("time", b.getTotalTime() + " giờ");

            // QUAN TRỌNG: map enum → FE status
            m.put("status", mapStatus(b.getBookingStatus()));

            return m;
        }).collect(Collectors.toList());
    }

    public Map<String, Object> getBookingDetail(Integer bookingId) {

        Booking b = bookingRepo.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking không tồn tại"));

        Map<String, Object> m = new HashMap<>();

        m.put("id", b.getBookingId());
        m.put("club", "CLB #" + b.getClubId());
        m.put("court", "Sân đã đặt");

        m.put("date", b.getCreatedAt()
                .toLocalDate()
                .toString());

        m.put("time", b.getTotalTime() + " giờ");

        m.put("totalPrice", b.getTotalPrice());
        m.put("paymentMethod", b.getPaymentMethod() != null
                ? b.getPaymentMethod().name()
                : null);

        m.put("note", b.getNote());
        m.put("status", mapStatus(b.getBookingStatus()));
        m.put("createdAt", b.getCreatedAt().toString());

        return m;
    }


    // ================== MAP STATUS ==================
    private String mapStatus(BookingStatus status) {
        if (status == null) return "pending";

        return switch (status) {
            case DANG_XU_LY -> "pending";
            case HOAN_THANH -> "completed";
            case HUY -> "cancelled";
        };
    }
    public List<Map<String, Object>> getAllBookings() {

        List<Booking> bookings = bookingRepo.findAll();

        return bookings.stream().map(b -> {
            Map<String, Object> m = new HashMap<>();

            m.put("id", b.getBookingId());
            m.put("profileId", b.getProfileId());
            m.put("club", "CLB #" + b.getClubId());
            m.put("court", "Sân đã đặt");
            m.put("date", b.getCreatedAt().toLocalDate().toString());
            m.put("time", b.getTotalTime() + " giờ");
            m.put("status", mapStatus(b.getBookingStatus()));

            return m;
        }).collect(Collectors.toList());
    }

    // admin duyệt / hủy
    @Transactional
    public void updateStatus(Integer bookingId, BookingStatus status) {

        Booking booking = bookingRepo.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking không tồn tại"));

        booking.setBookingStatus(status);
        bookingRepo.save(booking);
    }
}
