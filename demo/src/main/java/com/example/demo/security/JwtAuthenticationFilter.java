package com.example.demo.security;

import io.jsonwebtoken.*;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthenticationFilter implements Filter {

    private final String JWT_SECRET = "gizli_anahtar_buraya_gelecek";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String header = httpRequest.getHeader("Authorization");

        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);

            try {
                Claims claims = Jwts.parser()
                        .setSigningKey(JWT_SECRET)
                        .parseClaimsJws(token)
                        .getBody();

                String email = claims.getSubject();
                Integer roleId = claims.get("roleId", Integer.class);

                if (email != null && roleId != null) {
                    // roleId'yi ROLE_{roleId} formatına çevirip GrantedAuthority olarak ekleyelim
                    SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + roleId);
                    List<SimpleGrantedAuthority> authorities = List.of(authority);

                    UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                            email, null, authorities
                    );
                    auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpRequest));
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }

            } catch (ExpiredJwtException | MalformedJwtException | SignatureException e) {
                ((HttpServletResponse) response).sendError(HttpServletResponse.SC_UNAUTHORIZED, "Geçersiz token");
                return;
            }
        }

        chain.doFilter(request, response);
    }
}
