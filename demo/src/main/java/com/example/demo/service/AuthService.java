package com.example.demo.service;

import com.example.demo.dto.request.LoginRequest;
import com.example.demo.dto.request.SignupRequest;
import com.example.demo.dto.response.LoginResponse;
import com.example.demo.dto.response.SignupResponse;
import com.example.demo.model.members.Member;
import com.example.demo.repository.MemberRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    private final String JWT_SECRET = "gizli_anahtar_buraya_gelecek";
    private final long JWT_EXPIRATION_MS = 86400000; // 1 gün

    public SignupResponse signup(SignupRequest request) {
        if (memberRepository.existsByEmail(request.getEmail())) {
            return new SignupResponse("Bu email zaten kayıtlı!");
        }

        Member member = new Member();
        member.setName(request.getName());
        member.setSurname(request.getSurname());
        member.setEmail(request.getEmail());
        member.setMemberName(request.getMemberName());
        member.setPassword(passwordEncoder.encode(request.getPassword()));
        memberRepository.save(member);

        return new SignupResponse("Kayıt başarılı");
    }

    public LoginResponse login(LoginRequest request) {
        Member member = memberRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("Kullanıcı bulunamadı"));

        if (!passwordEncoder.matches(request.getPassword(), member.getPassword())) {
            throw new BadCredentialsException("Şifre hatalı");
        }

        String token = Jwts.builder()
                .setSubject(member.getEmail())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + JWT_EXPIRATION_MS))
                .signWith(SignatureAlgorithm.HS256, JWT_SECRET)
                .compact();

        return new LoginResponse(token, null);
    }
}
