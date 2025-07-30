package com.example.demo.config;

import com.example.demo.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // CORS ayarları
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                // CSRF devre dışı (JWT ile stateless)
                .csrf(csrf -> csrf.disable())

                // Stateless session yönetimi
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // Yetkilendirme kuralları
                .authorizeHttpRequests(auth -> auth
                        // Swagger ve dokümantasyon izinleri (public)
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/swagger-resources/**",
                                "/webjars/**",
                                "/api/auth/**"  // Auth endpointleri genelde public olur
                        ).permitAll()

                        // Workspace endpoint'ini public yap veya auth zorunluysa Swagger'da token gönder
                        // Eğer workspace oluşturma sadece girişli kullanıcıya açıksa aşağıdaki satırı kaldır ve Swagger'da token ile deneyin
                        .requestMatchers("/api/workspaces/**").permitAll()


                        // Admin endpointleri sadece ADMIN rolü
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")

                        // Member endpointleri sadece MEMBER rolü
                        .requestMatchers("/api/member/**").hasRole("MEMBER")

                        // Geri kalan tüm endpointler için kimlik doğrulama zorunlu
                        .anyRequest().authenticated()
                )

                // JWT filtresi UsernamePasswordAuthenticationFilter'dan önce
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList(
                "http://localhost:3000",
                "http://localhost:3001",
                "http://192.168.234.28:3000",
                "http://localhost:8080"  // Swagger UI portunu da eklemelisin
        ));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setExposedHeaders(Arrays.asList("Authorization", "Set-Cookie"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
