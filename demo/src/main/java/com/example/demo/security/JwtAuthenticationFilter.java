package com.example.demo.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

/**
 * JWT token doğrulama filtresi.
 * Bu filter gelen HTTP isteklerindeki JWT token'ları kontrol eder ve geçerliyse
 * kullanıcının kimlik bilgilerini Spring Security context'ine ekler.
 * 
 * Public endpoint'ler (örn: /api/auth/**) için token hatası olsa bile 
 * isteğin devam etmesine izin verir.
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter implements Filter {

    private final JwtUtil jwtUtil;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String requestPath = httpRequest.getRequestURI();
        String header = httpRequest.getHeader("Authorization");

        // Authorization header yoksa veya Bearer ile başlamıyorsa, isteği devam ettir
        if (header == null || !header.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }

        String token = header.substring(7);

        try {
            // JWT token'ı validate et
            if (jwtUtil.validateToken(token)) {
                Claims claims = jwtUtil.extractAllClaims(token);

                String email = claims.getSubject();
                Integer roleId = claims.get("roleId", Integer.class);
                String roleName = claims.get("roleName", String.class);
                String scope = claims.get("scope", String.class);

                // Tüm gerekli bilgiler varsa authentication context'ini oluştur
                if (email != null && roleId != null && roleName != null && scope != null) {
                    // roleName'i ROLE_{roleName} formatına çevir
                    SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + roleName.toUpperCase());
                    List<SimpleGrantedAuthority> authorities = List.of(authority);

                    UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                            email, null, authorities
                    );
                    auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpRequest));
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            }

        } catch (ExpiredJwtException | MalformedJwtException e) {
            // Public endpoint'ler için token hatası olsa bile isteği devam ettir
            if (isPublicEndpoint(requestPath)) {
                // Log the error but continue the request
                System.out.println("⚠️ JWT Token hatası (public endpoint için önemli değil): " + requestPath + " - " + e.getMessage());
                chain.doFilter(request, response);
                return;
            } else {
                // Private endpoint'ler için token hatası durumunda unauthorized dön
            ((HttpServletResponse) response).sendError(HttpServletResponse.SC_UNAUTHORIZED, "Geçersiz token");
            return;
        }
        } catch (Exception e) {
            // Genel hatalar için de aynı mantık
            if (isPublicEndpoint(requestPath)) {
                System.out.println("⚠️ JWT işleme hatası (public endpoint için önemli değil): " + requestPath + " - " + e.getMessage());
                chain.doFilter(request, response);
                return;
            } else {
                ((HttpServletResponse) response).sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Authentication failed");
                return;
            }
        }

        // İsteği devam ettir
        chain.doFilter(request, response);
    }

    /**
     * Belirtilen path'in public endpoint olup olmadığını kontrol eder
     * @param requestPath İstek path'i
     * @return Public endpoint ise true, değilse false
     */
    private boolean isPublicEndpoint(String requestPath) {
        return requestPath.startsWith("/api/auth/") ||
               requestPath.startsWith("/swagger-ui/") ||
               requestPath.startsWith("/v3/api-docs/") ||
               requestPath.startsWith("/swagger-resources/") ||
               requestPath.startsWith("/webjars/");
    }
}
