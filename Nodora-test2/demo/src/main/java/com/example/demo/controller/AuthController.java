package com.example.demo.controller;

import com.example.demo.dto.request.ForgotPasswordRequest;
import com.example.demo.dto.request.LoginRequest;
import com.example.demo.dto.request.SignupRequest;
import com.example.demo.dto.response.ForgotPasswordResponse;
import com.example.demo.dto.response.LoginResponse;
import com.example.demo.dto.response.SignupResponse;
import com.example.demo.model.members.Member;
import com.example.demo.repository.MemberRepository;
import com.example.demo.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
//import org.springframework.mail.SimpleMailMessage;
//import org.springframework.mail.javamail.JavaMailSender;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
//    private final JavaMailSender mailSender; // Email göndermek için

    @PostMapping("/signup")
    public ResponseEntity<SignupResponse> signup(@Valid @RequestBody SignupRequest request) {
        SignupResponse response = authService.signup(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ForgotPasswordResponse> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        Optional<Member> memberOptional = memberRepository.findByEmail(request.getEmail());

        if (memberOptional.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(new ForgotPasswordResponse("Bu e-posta adresi ile kayıtlı kullanıcı bulunamadı."));
        }

        Member member = memberOptional.get();

        // Rastgele şifre oluştur
        String newPassword = generateRandomPassword(8);

        // Şifreyi hashle ve kaydet
        member.setPassword(passwordEncoder.encode(newPassword));
        memberRepository.save(member);

//        // Email gönder
//        sendPasswordEmail(member.getEmail(), newPassword);

        return ResponseEntity.ok(new ForgotPasswordResponse("Yeni şifreniz e-posta adresinize gönderildi."));
    }

    @PostMapping("/create-admin")
    public ResponseEntity<SignupResponse> createAdmin(@Valid @RequestBody SignupRequest request) {
        if (memberRepository.existsByEmail(request.getEmail())) {
            return ResponseEntity.badRequest().body(new SignupResponse("Bu email zaten kayıtlı!"));
        }

        Member member = new Member();
        member.setName(request.getName());
        member.setSurname(request.getSurname());
        member.setEmail(request.getEmail());
        member.setMemberName(request.getMemberName());
        member.setPassword(passwordEncoder.encode(request.getPassword()));
        member.setIsAdmin(true);
        member.setMembersActive(true);
        memberRepository.save(member);

        return ResponseEntity.ok(new SignupResponse("Admin kullanıcısı başarıyla oluşturuldu"));
    }

    @GetMapping("/users")
    public ResponseEntity<List<Member>> getAllUsers() {
        List<Member> users = memberRepository.findAll();
        return ResponseEntity.ok(users);
    }

    private String generateRandomPassword(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }

        return sb.toString();
    }

//   private void sendPasswordEmail(String email, String newPassword) {
//        SimpleMailMessage message = new SimpleMailMessage();
//        message.setTo(email);
//        message.setSubject("Şifre Sıfırlama");
//        message.setText("Yeni şifreniz: " + newPassword + "\n\nGüvenlik nedeniyle bu şifreyi ilk girişinizde değiştirmeniz önerilir.");
//
//        mailSender.send(message);
//    }
}