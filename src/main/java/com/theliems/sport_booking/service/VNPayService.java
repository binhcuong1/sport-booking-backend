package com.theliems.sport_booking.service;

import org.springframework.beans.factory.annotation.Value;

import com.theliems.sport_booking.util.VNPayUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class VNPayService {

    @Value("${vnpay.tmn-code}")
    private String tmnCode;

    @Value("${vnpay.hash-secret}")
    private String secretKey;

    @Value("${vnpay.url}")
    private String vnpUrl;

    @Value("${vnpay.return-url}")
    private String returnUrl;

    public Map<String, Object> createPayment(
            Long bookingId,
            Long amount,
            HttpServletRequest request
    ) {

        if (bookingId == null || amount == null) {
            throw new IllegalArgumentException("amount và bookingId là bắt buộc");
        }

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

        String createDate = now.format(formatter);
        String expireDate = now.plusMinutes(15).format(formatter);

        String ipAddr = request.getHeader("X-FORWARDED-FOR");
        if (ipAddr == null) {
            ipAddr = request.getRemoteAddr();
        }

        // orderId = bookingId_timestamp
        String orderId = bookingId + "_" + System.currentTimeMillis();

        Map<String, String> params = new HashMap<>();
        params.put("vnp_Version", "2.1.0");
        params.put("vnp_Command", "pay");
        params.put("vnp_TmnCode", tmnCode);
        params.put("vnp_Amount", String.valueOf(amount * 100));
        params.put("vnp_CurrCode", "VND");
        params.put("vnp_TxnRef", orderId);
        params.put("vnp_OrderInfo", "Thanh toan booking " + bookingId);
        params.put("vnp_OrderType", "other");
        params.put("vnp_Locale", "vn");
        params.put("vnp_ReturnUrl", returnUrl);
        params.put("vnp_IpAddr", ipAddr);
        params.put("vnp_CreateDate", createDate);
        params.put("vnp_ExpireDate", expireDate);

        // sort alphabet
        Map<String, String> sorted = VNPayUtil.sortParams(params);

        // build query string
        String query = sorted.entrySet().stream()
                .map(e -> e.getKey() + "=" +
                        URLEncoder.encode(e.getValue(), StandardCharsets.UTF_8))
                .collect(Collectors.joining("&"));

        // sign
        String secureHash = VNPayUtil.hmacSHA512(secretKey, query);

        String paymentUrl = vnpUrl
                + "?" + query
                + "&vnp_SecureHashType=HMACSHA512"
                + "&vnp_SecureHash=" + secureHash;

        return Map.of(
                "paymentUrl", paymentUrl,
                "bookingId", bookingId,
                "orderId", orderId
        );
    }

    public String handleReturn(HttpServletRequest request) {

        String responseCode = request.getParameter("vnp_ResponseCode");
        String txnRef = request.getParameter("vnp_TxnRef");

        if ("00".equals(responseCode) && txnRef != null) {
            String bookingId = txnRef.split("_")[0];
            return "" + bookingId;
        }
        return "";
    }
    public String buildReturnRedirectUrl(HttpServletRequest request) {

        String responseCode = request.getParameter("vnp_ResponseCode");
        String txnRef = request.getParameter("vnp_TxnRef");
        String amount = request.getParameter("vnp_Amount");

        if (!"00".equals(responseCode) || txnRef == null) {
            return "http://localhost:5500/customer/payment-fail.html";
        }

        String bookingId = txnRef.split("_")[0];

        return "http://127.0.0.1:5500/customer/pages/payment-success.html"
                + "?bookingId=" + bookingId
                + "&amount=" + (Long.parseLong(amount) / 100)
                + "&payMethod=VNPay";
    }


}
