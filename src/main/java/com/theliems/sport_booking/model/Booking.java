package com.theliems.sport_booking.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Booking")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer booking_id;

    @ManyToOne
    @JoinColumn(name = "club_id")
    private Club club;

    private Integer total_time;
    private Double total_price;

    @Column(columnDefinition = "TEXT")
    private String note;

    private String phone_number;

    @Enumerated(EnumType.STRING)
    private PaymentMethod payment_method;

    public enum PaymentMethod {
        vnpay, momo
    }

    @Enumerated(EnumType.STRING)
    private BookingStatus booking_status = BookingStatus.pending;

    public enum BookingStatus {
        pending, completed, cancelled
    }

    @ManyToOne
    @JoinColumn(name = "profile_id")
    private Profile profile;

    private LocalDateTime created_at = LocalDateTime.now();
}
