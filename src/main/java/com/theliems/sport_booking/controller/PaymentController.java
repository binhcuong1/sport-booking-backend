package com.theliems.sport_booking.controller;

import com.theliems.sport_booking.model.Booking;
import com.theliems.sport_booking.repository.BookingRepository;
import com.theliems.sport_booking.service.VNPayService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final VNPayService vnpayService;
    private final BookingRepository bookingRepo;

    @PostMapping("/vnpay/create")
    public ResponseEntity<?> createPayment(
            @RequestBody Map<String, Long> body,
            HttpServletRequest request
    ) {
        Long bookingId = body.get("bookingId");

        Booking booking = bookingRepo.findById(Math.toIntExact(bookingId))
                .orElseThrow(() -> new RuntimeException("Booking không tồn tại"));

        return ResponseEntity.ok(
                vnpayService.createPayment(
                        bookingId,
                        booking.getTotalPrice().longValue(),
                        request
                )
        );
    }

    @GetMapping("/vnpay/return")
    public void vnpayReturn(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {

        String redirectUrl = vnpayService.buildReturnRedirectUrl(request);
        response.sendRedirect(redirectUrl);
    }



}
