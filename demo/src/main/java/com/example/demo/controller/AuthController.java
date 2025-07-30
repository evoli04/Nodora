package com.example.demo.controller;

import com.example.demo.dto.request.ForgotPasswordRequest;
import com.example.demo.dto.request.GoogleAuthRequest;
import com.example.demo.dto.request.LoginRequest;
import com.example.demo.dto.request.SignupRequest;
import com.example.demo.dto.response.ForgotPasswordResponse;
import com.example.demo.dto.response.GoogleAuthResponse;
import com.example.demo.dto.response.LoginResponse;
import com.example.demo.dto.response.SignupResponse;
import com.example.demo.model.members.Member;
import com.example.demo.repository.MemberRepository;
import com.example.demo.service.AuthService;
import com.example.demo.service.EmailService;
import com.example.demo.service.GoogleAuthService;
import com.example.demo.service.GmailOAuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

/**
 * Kimlik doğrulama işlemlerini yöneten Controller sınıfı.
 * Bu sınıf kullanıcı kayıt, giriş ve şifre sıfırlama işlemlerini yönetir.
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;
    private final GoogleAuthService googleAuthService;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService; // Outlook SMTP servisi
    private final GmailOAuthService gmailOAuthService; // Gmail OAuth 2.0 servisi

    // JavaMailSender artık EmailService içinde kullanılıyor - burada gerek yok

    /**
     * Kullanıcı kayıt endpoint'i
     */
    @PostMapping("/signup")
    public ResponseEntity<SignupResponse> signup(@Valid @RequestBody SignupRequest request) {
        SignupResponse response = authService.signup(request);
        return ResponseEntity.ok(response);
    }

    /**
     * Kullanıcı giriş endpoint'i
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    /**
     * Google OAuth ile giriş/kayıt endpoint'i
     */
    @PostMapping("/google")
    public ResponseEntity<?> googleAuth(@Valid @RequestBody GoogleAuthRequest request) {
        try {
            GoogleAuthResponse response = googleAuthService.authenticateWithGoogle(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Google OAuth endpoint'inde hata: {}", e.getMessage(), e);
            return ResponseEntity.badRequest()
                    .body(new GoogleAuthResponse(null, null, null, "Google ile giriş yapılamadı: " + e.getMessage(), false));
        }
    }

    /**
     * ŞİFREMİ UNUTTUM ENDPOINT'İ
     * Bu endpoint tüm şifre sıfırlama sürecini yönetir:
     * 1. E-posta veritabanında var mı kontrol eder
     * 2. Yeni rastgele şifre oluşturur
     * 3. Şifreyi hashleyerek veritabanına kaydeder
     * 4. Kullanıcıya e-posta ile gönderir (Gmail API öncelikli, SMTP fallback)
     * 5. Frontend'e başarı mesajı döner
     */
    @PostMapping("/forgot-password")
    public ResponseEntity<ForgotPasswordResponse> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        
        try {
            // 1. ADIM: E-posta veritabanında var mı kontrol et
        Optional<Member> memberOptional = memberRepository.findByEmail(request.getEmail());

            // E-posta bulunamazsa hata mesajı dön
        if (memberOptional.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(new ForgotPasswordResponse("Bu e-posta adresi ile kayıtlı kullanıcı bulunamadı."));
        }

        Member member = memberOptional.get();

            // 2. ADIM: Yeni rastgele şifre oluştur (8 karakter)
        String newPassword = generateRandomPassword(8);

            // 3. ADIM: Şifreyi hashle (BCrypt ile) ve veritabanına kaydet
        member.setPassword(passwordEncoder.encode(newPassword));
        memberRepository.save(member);

            // 4. ADIM: E-posta gönderimi (Gmail API öncelikli, SMTP fallback)
            String emailResult = sendPasswordEmail(member.getEmail(), newPassword);

            return ResponseEntity.ok(new ForgotPasswordResponse(emailResult));
            
        } catch (Exception e) {
            // Genel hatalar için log yaz ve kullanıcı dostu mesaj dön
            System.err.println("🚨 Forgot password endpoint'inde hata: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500)
                    .body(new ForgotPasswordResponse("Şifre sıfırlama sırasında bir hata oluştu. Lütfen daha sonra tekrar deneyin."));
    }
    }

    /**
     * Admin kullanıcısı oluşturma endpoint'i
     */
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

    /**
     * Tüm kullanıcıları listele endpoint'i
     */
    @GetMapping("/users")
    public ResponseEntity<List<Member>> getAllUsers() {
        List<Member> users = memberRepository.findAll();
        return ResponseEntity.ok(users);
    }

    /**
     * Basit test endpoint'i - JWT sorunlarını debug etmek için
     */
    @GetMapping("/test")
    public ResponseEntity<Map<String, String>> testEndpoint() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Auth endpoint çalışıyor");
        response.put("time", String.valueOf(System.currentTimeMillis()));
        response.put("gmailAPI", "devre dışı"); // Gmail API kaldırıldı
        response.put("smtpService", emailService.isAvailable() ? "aktif" : "devre dışı");
        response.put("googleOAuth", "aktif");
        response.put("googleClientId", googleAuthService != null ? "yüklendi" : "yüklenmedi");
        response.put("gmailOAuth", gmailOAuthService.isAvailable() ? "aktif" : "devre dışı");
        return ResponseEntity.ok(response);
    }

    /**
     * Google OAuth test endpoint'i
     */
    @GetMapping("/google/test")
    public ResponseEntity<Map<String, String>> googleTestEndpoint() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Google OAuth endpoint çalışıyor");
        response.put("endpoint", "/api/auth/google");
        response.put("method", "POST");
        return ResponseEntity.ok(response);
    }

    /**
     * Google OAuth debug endpoint'i - gelen veriyi kontrol etmek için
     */
    @PostMapping("/google/debug")
    public ResponseEntity<Map<String, Object>> googleDebugEndpoint(@RequestBody GoogleAuthRequest request) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "debug");
        response.put("idToken", request.getIdToken() != null ? request.getIdToken().substring(0, Math.min(50, request.getIdToken().length())) + "..." : "null");
        response.put("accessToken", request.getAccessToken() != null ? request.getAccessToken().substring(0, Math.min(50, request.getAccessToken().length())) + "..." : "null");
        response.put("idTokenLength", request.getIdToken() != null ? request.getIdToken().length() : 0);
        response.put("accessTokenLength", request.getAccessToken() != null ? request.getAccessToken().length() : 0);
        return ResponseEntity.ok(response);
    }

    /**
     * Gmail OAuth 2.0 ile test e-posta gönderme endpoint'i
     * Bu endpoint ilk çalıştırıldığında tarayıcıda Google authorization sayfası açılır
     */
    @PostMapping("/test-email")
    public ResponseEntity<Map<String, String>> testEmail(
            @RequestParam String toEmail,
            @RequestParam String fromEmail,
            @RequestParam(required = false, defaultValue = "Nodora Test") String subject,
            @RequestParam(required = false, defaultValue = "Bu bir test mesajıdır.") String message) {
        
        Map<String, String> response = new HashMap<>();
        
        try {
            System.out.println("🧪 Gmail OAuth test email gönderimi başlatılıyor...");
            
            gmailOAuthService.sendEmail(toEmail, subject, message, fromEmail);
            
            response.put("status", "success");
            response.put("message", "Test e-posta başarıyla gönderildi!");
            response.put("to", toEmail);
            response.put("from", fromEmail);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            System.err.println("❌ Test email gönderim hatası: " + e.getMessage());
            
            response.put("status", "error");
            response.put("message", "E-posta gönderim hatası: " + e.getMessage());
            response.put("error", e.getClass().getSimpleName());
            
            return ResponseEntity.status(500).body(response);
        }
    }

    /**
     * Rastgele şifre oluşturan yardımcı metot
     * @param length Oluşturulacak şifrenin uzunluğu
     * @return Rastgele oluşturulmuş şifre
     */
    private String generateRandomPassword(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }

        return sb.toString();
    }

    /**
     * E-posta gönderen yardımcı metot (Sadece Gmail API kullanır)
     * @param email Gönderilecek e-posta adresi
     * @param newPassword Gönderilecek yeni şifre
     * @return Sonuç mesajı
     */
    private String sendPasswordEmail(String email, String newPassword) {
        String subject = "Şifre Sıfırlama";
        String body = "Yeni şifreniz: " + newPassword + "\n\nGüvenlik nedeniyle bu şifreyi ilk girişinizde değiştirmeniz önerilir.";
        
        // Outlook SMTP ile göndermeyi dene
        if (emailService.isAvailable()) {
            try {
                System.out.println("🎯 Outlook SMTP ile e-posta gönderimi deneniyor...");
                emailService.sendEmail(email, subject, body);
                return "Yeni şifreniz e-posta adresinize gönderildi. [Outlook SMTP]";
            } catch (Exception e) {
                System.err.println("🚨 Outlook SMTP DETAYLI HATA:");
                System.err.println("   Hata türü: " + e.getClass().getSimpleName());
                System.err.println("   Hata mesajı: " + e.getMessage());
                if (e.getCause() != null) {
                    System.err.println("   Cause: " + e.getCause().getMessage());
                }
                
                // Outlook SMTP hatası durumunda detaylı bilgi dön
                return "Outlook SMTP hatası: " + e.getMessage() + ". SMTP ayarlarını kontrol edin.";
            }
        } else {
            return "Outlook SMTP servis mevcut değil. Konfigürasyon hatası.";
        }
    }


}