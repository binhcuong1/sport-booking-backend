package com.theliems.sport_booking.service;

import com.theliems.sport_booking.model.Account;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
public class JwtService {

    // 🔑 SECRET KEY (demo – đưa vào env khi production)
    private static final String SECRET =
            "sport-booking-secret-key-12345678901234567890";

    private static final long EXPIRATION = 1000 * 60 * 60 * 24; // 1 ngày

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET.getBytes());
    }

    // ================== GENERATE ==================
    public String generateToken(Account account) {

        return Jwts.builder()
                .setSubject(account.getEmail())
                .claim("role", account.getRole().name())
                .claim("accountId", account.getAccountId())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // ================== VALIDATE ==================
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    // ================== EXTRACT ==================
    public Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String extractEmail(String token) {
        return extractClaims(token).getSubject();
    }

    public String extractRole(String token) {
        return extractClaims(token).get("role", String.class);
    }
}
