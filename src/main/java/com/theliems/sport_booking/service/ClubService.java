package com.theliems.sport_booking.service;

import com.theliems.sport_booking.model.Club;
import com.theliems.sport_booking.repository.ClubRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClubService {

    @Autowired
    private ClubRepository repository;

    // ================= USER =================

    // User / Guest: xem danh sách club (hiển thị bản đồ)
    public List<Club> getAll() {
        return repository.findByIsDeletedFalse();
    }

    public Club getById(Integer id) {
        return repository.findById(id).orElse(null);
    }

    // ================= ADMIN =================

    public Club create(Club club) {

        club.setClubId(null);
        club.setIsDeleted(false);

        if (club.getAddress() == null || club.getAddress().isBlank()) {
            throw new RuntimeException("Địa chỉ không hợp lệ");
        }

        /*
         * QUY ƯỚC:
         * - FE phải gửi latitude & longitude nếu muốn hiển thị map
         * - BE KHÔNG geocode
         * - Không có tọa độ → club vẫn được tạo nhưng KHÔNG hiện trên map
         */

        return repository.save(club);
    }

    public Club update(Integer id, Club club) {

        Club existing = repository.findById(id).orElse(null);
        if (existing == null) return null;

        existing.setClubName(club.getClubName());
        existing.setDescription(club.getDescription());
        existing.setContactPhone(club.getContactPhone());
        existing.setOpenTime(club.getOpenTime());
        existing.setCloseTime(club.getCloseTime());
        existing.setAddress(club.getAddress());

        // 🔹 FE gửi lat/lng → update
        existing.setLatitude(club.getLatitude());
        existing.setLongitude(club.getLongitude());

        return repository.save(existing);
    }

    // ================= DELETE =================

    // Soft delete (chuẩn đồ án)
    public void delete(Integer id) {
        Club club = repository.findById(id).orElse(null);
        if (club == null) return;

        club.setIsDeleted(true);
        repository.save(club);
    }
}
