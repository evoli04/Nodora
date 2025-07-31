package com.example.demo.service;

import com.example.demo.dto.request.GoogleAuthRequest;
import com.example.demo.dto.response.GoogleAuthResponse;
import com.example.demo.model.members.Member;
import com.example.demo.repository.MemberRepository;
import com.example.demo.security.JwtUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class GoogleAuthServiceImpl implements GoogleAuthService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Value("${google.oauth.client.id}")
    private String googleClientId;

    @Override
    public GoogleAuthResponse authenticateWithGoogle(GoogleAuthRequest request) {
        try {
            log.info("Google OAuth authentication başlatılıyor");
            log.info("Request ID Token: {}", request.getIdToken());
            log.info("Request Access Token: {}", request.getAccessToken());

            // Test için basit bir email kullan
            String email = "test@google.com";
            String givenName = "Google";
            String familyName = "User";

            log.info("Test kullanıcı bilgileri: {}", email);

            // Kullanıcı veritabanında var mı kontrol et
            Optional<Member> existingMember = memberRepository.findByEmail(email);
            boolean isNewUser = false;
            Member member;

            if (existingMember.isPresent()) {
                // Mevcut kullanıcı
                member = existingMember.get();
                log.info("Mevcut kullanıcı bulundu: {}", email);
            } else {
                // Yeni kullanıcı oluştur
                member = new Member();
                member.setEmail(email);
                member.setName(givenName);
                member.setSurname(familyName);
                member.setMemberName(email.split("@")[0]);
                member.setPassword(passwordEncoder.encode("google_oauth_" + System.currentTimeMillis()));
                member.setIsAdmin(false);
                member.setMembersActive(true);

                member = memberRepository.save(member);
                isNewUser = true;
                log.info("Yeni kullanıcı oluşturuldu: {}", email);
            }

            // JWT token oluştur
            String roleName = member.getIsAdmin() ? "ADMIN" : "MEMBER";
            Integer roleId = member.getIsAdmin() ? 1 : 4;
            String scope = member.getIsAdmin() ? "GLOBAL" : "WORKSPACE";

            String token = jwtUtil.generateToken(member.getEmail(), roleId, roleName, scope);

            log.info("Google OAuth başarılı: {}, Yeni kullanıcı: {}", email, isNewUser);

            return new GoogleAuthResponse(token, roleId, member.getMemberId(),email, isNewUser);

        } catch (Exception e) {
            log.error("Google OAuth sırasında hata: {}", e.getMessage(), e);
            throw new RuntimeException("Google ile giriş yapılamadı: " + e.getMessage());
        }
    }
} 