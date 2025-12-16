package com.theliems.sport_booking.repository;

import com.theliems.sport_booking.model.OtpVerification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OtpVerificationRepository
        extends JpaRepository<OtpVerification, Integer> {

    Optional<OtpVerification> findByEmail(String email);

    void deleteByEmail(String email);
    boolean existsByEmail(String email);
}
