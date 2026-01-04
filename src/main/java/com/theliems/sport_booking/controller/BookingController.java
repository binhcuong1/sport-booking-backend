package com.theliems.sport_booking.controller;

import com.theliems.sport_booking.model.BookingStatus;
import com.theliems.sport_booking.service.BookingService;
import com.theliems.sport_booking.service.CreateBookingRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;
    @PostMapping
    public ResponseEntity<?> createBooking(
            @RequestBody CreateBookingRequest request
    ) {
        Integer bookingId = bookingService.createBooking(request);
        return ResponseEntity.ok(
                Map.of(
                        "success", true,
                        "bookingId", bookingId
                )
        );
    }

    @GetMapping("/history")
    public ResponseEntity<?> getBookingHistory(
            @RequestParam Integer profileId
    ) {
        return ResponseEntity.ok(
                bookingService.getBookingHistory(profileId)
        );
    }

    // admin xem TẤT CẢ đơn
    @GetMapping("/all")
    public ResponseEntity<?> getAllBookings() {
        return ResponseEntity.ok(
                bookingService.getAllBookings()
        );
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getBookingDetail(@PathVariable Integer id) {
        return ResponseEntity.ok(
                bookingService.getBookingDetail(id)
        );
    }


    @PutMapping("/{id}/approve")
    public ResponseEntity<?> approveBooking(@PathVariable Integer id) {
        bookingService.updateStatus(id, BookingStatus.HOAN_THANH);
        return ResponseEntity.ok(Map.of("success", true));
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<?> cancelBooking(@PathVariable Integer id) {
        bookingService.updateStatus(id, BookingStatus.HUY);
        return ResponseEntity.ok(Map.of("success", true));
    }

}
