package com.theliems.sport_booking.service;

import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import org.springframework.beans.factory.annotation.Value;
import com.theliems.sport_booking.model.Account;
import com.theliems.sport_booking.model.OtpVerification;
import com.theliems.sport_booking.repository.AccountRepository;
import com.theliems.sport_booking.repository.OtpVerificationRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.theliems.sport_booking.model.Profile;
import com.theliems.sport_booking.repository.ProfileRepository;


import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Random;

@Service
public class AuthService {
    private final AccountRepository accountRepository;
    private final OtpVerificationRepository otpVerificationRepository;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;
    @Value("${google.client-id}")
    private String googleClientId;
    private final ProfileRepository profileRepository;


    public AuthService(
            AccountRepository accountRepository,
            OtpVerificationRepository otpVerificationRepository,
            PasswordEncoder passwordEncoder,
            MailService mailService,
            @Value("${google.client-id}") String googleClientId,
            ProfileRepository profileRepository
    ) {
        this.accountRepository = accountRepository;
        this.otpVerificationRepository = otpVerificationRepository;
        this.passwordEncoder = passwordEncoder;
        this.mailService = mailService;
        this.googleClientId = googleClientId;
        this.profileRepository = profileRepository;
    }
    public void register(String email, String password) {

        if (accountRepository.existsByEmail(email)) {
            throw new RuntimeException("Email đã tồn tại");
        }

        // tạo account
        Account account = new Account();
        account.setEmail(email);
        account.setPassword(passwordEncoder.encode(password));
        account.setRole(Account.Role.user);
        accountRepository.save(account);

        // xoá OTP cũ
        otpVerificationRepository.deleteByEmail(email);

        // tao OTP
        String otp = String.valueOf(100000 + new Random().nextInt(900000));

        OtpVerification otpEntity = new OtpVerification();
        otpEntity.setEmail(email);
        otpEntity.setOtpHash(passwordEncoder.encode(otp));
        otpEntity.setExpiredAt(LocalDateTime.now().plusMinutes(5));
        otpVerificationRepository.save(otpEntity);

        //gửi email
        mailService.sendOtp(email, otp);
    }
    private void createDefaultProfile(Account account) {

        // tránh tạo trùng
        if (profileRepository.findByAccount_AccountId(account.getAccountId()).isPresent()) {
            return;
        }

        Profile profile = new Profile();
        profile.setAccount(account);
        profile.setGender(Profile.Gender.unknown);
        profile.setIs_deleted(false);

        profileRepository.save(profile);
    }


    // verify otp
    public void verifyOtp(String email, String otp) {

        OtpVerification otpEntity = otpVerificationRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("OTP không tồn tại"));

        // hết hạn → xoá
        if (otpEntity.getExpiredAt().isBefore(LocalDateTime.now())) {
            otpVerificationRepository.delete(otpEntity);
            throw new RuntimeException("OTP đã hết hạn");
        }

        // so sánh OTP
        if (!passwordEncoder.matches(otp, otpEntity.getOtpHash())) {
            throw new RuntimeException("OTP không đúng");
        }
        //  OTP đúng → xoá
        otpVerificationRepository.delete(otpEntity);
        Account account = accountRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Account không tồn tại"));

        createDefaultProfile(account);
    }

    // login
    public Account login(String email, String password) {

        // 1. kiểm tra email tồn tại
        Account account = accountRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Email không tồn tại"));

        // 2. kiểm tra đã xác thực OTP chưa
        // nếu còn OTP trong bảng otp_verification => CHƯA verify
        if (otpVerificationRepository.existsByEmail(email)) {
            throw new RuntimeException("Tài khoản chưa xác thực OTP");
        }

        // 3. kiểm tra mật khẩu
        if (!passwordEncoder.matches(password, account.getPassword())) {
            throw new RuntimeException("Mật khẩu không đúng");
        }

        // 4. login thành công
        return account;
    }
    public Account loginWithGoogle(String idTokenString) {

        try {
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                    GoogleNetHttpTransport.newTrustedTransport(),
                    JacksonFactory.getDefaultInstance()
            )
                    .setAudience(Collections.singletonList(googleClientId))
                    .build();

            GoogleIdToken idToken = verifier.verify(idTokenString);

            if (idToken == null) {
                throw new RuntimeException("Invalid Google ID Token");
            }

            GoogleIdToken.Payload payload = idToken.getPayload();
            String email = payload.getEmail();

            return accountRepository.findByEmail(email)
                    .orElseGet(() -> {
                        Account acc = new Account();
                        acc.setEmail(email);
                        acc.setRole(Account.Role.user);
                        acc.setPassword("GOOGLE_LOGIN");
                        accountRepository.save(acc);
                        createDefaultProfile(acc);

                        return acc;
                    });


        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Google login failed");
        }
    }





}
