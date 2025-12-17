package com.theliems.sport_booking.repository;

import com.theliems.sport_booking.model.ServiceType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceTypeRepository extends JpaRepository<ServiceType, Integer> {
}
