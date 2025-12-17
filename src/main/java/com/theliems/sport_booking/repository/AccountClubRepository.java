package com.theliems.sport_booking.repository;

import com.theliems.sport_booking.model.AccountClub;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AccountClubRepository extends JpaRepository<AccountClub, Integer> {
    List<AccountClub> findByAccountIdAndIsDeletedFalse(Integer accountId);
}

