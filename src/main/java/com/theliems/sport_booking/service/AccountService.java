package com.theliems.sport_booking.service;

import com.theliems.sport_booking.model.Account;
import com.theliems.sport_booking.repository.AccountRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    public AccountService(AccountRepository repository, PasswordEncoder passwordEncoder) {
        this.accountRepository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<Account> getAll() {
        return accountRepository.findAll();
    }

    public Account getById(Integer id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy tài khoản"));
    }

    public Account create(Account account) {
        if (accountRepository.existsByEmail(account.getEmail())) {
            throw new RuntimeException("Email đã tồn tại");
        }
        //bcrypt
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        return accountRepository.save(account);
    }

    public Account update(Integer id, Account account) {
        Account existing = getById(id);
        existing.setEmail(account.getEmail());
        existing.setRole(account.getRole());
        if (account.getPassword() != null && account.getPassword().isBlank()){
            existing.setPassword(passwordEncoder.encode(account.getPassword()));
        }
        return accountRepository.save(existing);
    }

    public void delete(Integer id) {
        if (!accountRepository.existsById(id)) {
            throw new RuntimeException("Không tìm thấy tài khoản");
        }
        accountRepository.deleteById(id);

    }
}

