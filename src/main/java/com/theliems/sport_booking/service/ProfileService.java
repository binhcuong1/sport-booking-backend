package com.theliems.sport_booking.service;

import com.theliems.sport_booking.model.Account;
import com.theliems.sport_booking.model.Profile;
import com.theliems.sport_booking.repository.AccountRepository;
import com.theliems.sport_booking.repository.ProfileRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProfileService {

    private final ProfileRepository profileRepository;
    private final AccountRepository accountRepository;

    public ProfileService(
            ProfileRepository profileRepository,
            AccountRepository accountRepository
    ) {
        this.profileRepository = profileRepository;
        this.accountRepository = accountRepository;
    }

    // GET ALL
    public List<Profile> getAll() {
        return profileRepository.findAll();
    }

    // GET BY ID
    public Profile getById(Integer id) {
        return profileRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Profile không tồn tại"));
    }

    // GET BY ACCOUNT
    public Profile getByAccountId(Integer accountId) {
        return profileRepository.findByAccount_AccountId(accountId)
                .orElseThrow(() -> new RuntimeException("Profile không tồn tại"));
    }

    // CREATE
    public Profile create(Integer accountId, Profile profile) {

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account không tồn tại"));

        profile.setAccount(account);
        return profileRepository.save(profile);
    }

    // UPDATE
    public Profile update(Integer id, Profile data) {

        Profile profile = getById(id);

        profile.setFullname(data.getFullname());
        profile.setGender(data.getGender());
        profile.setAvatar_url(data.getAvatar_url());

        return profileRepository.save(profile);
    }

    // SOFT DELETE
    public void delete(Integer id) {

        Profile profile = getById(id);
        profile.setIs_deleted(true);
        profileRepository.save(profile);
    }
}
