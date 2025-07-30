package com.example.demo.service;

import com.example.demo.dto.request.LoginRequest;
import com.example.demo.dto.request.SignupRequest;
import com.example.demo.dto.response.LoginResponse;
import com.example.demo.dto.response.SignupResponse;
import com.example.demo.model.members.Member;
import com.example.demo.repository.MemberRepository;
import com.example.demo.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public SignupResponse signup(SignupRequest request) {
        log.info("Signup isteği: {}", request.getEmail());
        
        if (memberRepository.existsByEmail(request.getEmail())) {
            log.warn("Email zaten kayıtlı: {}", request.getEmail());
            return new SignupResponse("Bu email zaten kayıtlı!");
        }

        Member member = new Member();
        member.setName(request.getName());
        member.setSurname(request.getSurname());
        member.setEmail(request.getEmail());
        member.setMemberName(request.getMemberName());
        member.setPassword(passwordEncoder.encode(request.getPassword()));
        member.setIsAdmin(false); // Varsayılan olarak admin değil
        member.setMembersActive(true); // Aktif kullanıcı
        memberRepository.save(member);

        log.info("Kullanıcı başarıyla kaydedildi: {}", request.getEmail());
        return new SignupResponse("Kayıt başarılı");
    }

    public LoginResponse login(LoginRequest request) {
        try {
            log.info("Login isteği: {}", request.getEmail());
            
            Member member = memberRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> {
                        log.error("Kullanıcı bulunamadı: {}", request.getEmail());
                        return new UsernameNotFoundException("Kullanıcı bulunamadı");
                    });

            log.info("Kullanıcı bulundu: {}, isAdmin: {}", request.getEmail(), member.getIsAdmin());

            if (!passwordEncoder.matches(request.getPassword(), member.getPassword())) {
                log.error("Şifre hatalı: {}", request.getEmail());
                throw new BadCredentialsException("Şifre hatalı");
            }

            log.info("Şifre doğrulandı: {}", request.getEmail());

            // is_admin'e göre role belirleme
            String roleName = member.getIsAdmin() ? "ADMIN" : "MEMBER";
            Integer roleId = member.getIsAdmin() ? 1 : 4; // 1: ADMIN, 4: MEMBER (WORKSPACE)
            String scope = member.getIsAdmin() ? "GLOBAL" : "WORKSPACE";

            String token = jwtUtil.generateToken(member.getEmail(), roleId, roleName, scope);

            log.info("JWT token oluşturuldu: {}, role: {}", request.getEmail(), roleName);
            return new LoginResponse(token, roleId, member.getMemberId());
            
        } catch (Exception e) {
            log.error("Login sırasında hata: {}", e.getMessage(), e);
            throw e;
        }
    }
}
