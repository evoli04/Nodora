package com.example.demo.config;

import com.example.demo.model.members.Member;
import com.example.demo.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Order(1)
public class DataInitializer implements CommandLineRunner {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Mevcut admin kullanıcısının şifresini hashle
        Member adminUser = memberRepository.findByEmail("admin@example.com").orElse(null);
        if (adminUser != null && adminUser.getPassword().equals("123456")) {
            adminUser.setPassword(passwordEncoder.encode("123456"));
            memberRepository.save(adminUser);
            System.out.println("Admin kullanıcısının şifresi hashlenmiş!");
        }
    }
} 