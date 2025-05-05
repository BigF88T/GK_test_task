package ru.safonov.test_task.security.expression;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import ru.safonov.test_task.services.UserService;

import java.security.Key;

@Service
@RequiredArgsConstructor
public class JwtTokenProvider {
    private final JwtProperties jwtProperties;
    private final JwtUserDetailsService userDetailsService;
    private final UserService userService;
    private Key key;
    private long expirationTime;

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes());
        this.expirationTime = jwtProperties.getExpirationTime();
    }

    public String createAccessToken(Long userId, String email) {
        Claims claims = Jwts.claims().setSubject(email);
        claims.put("id",userId);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new java.util.Date())
                .setExpiration(new java.util.Date(System.currentTimeMillis() + expirationTime))
                .signWith(Keys.hmacShaKeyFor(key.getEncoded()))
                .compact();
    }

    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer "))
            return bearerToken.substring(7);
        return null;
    }

    public boolean validateToken(String token) {
        Jws<Claims> claims = Jwts
                .parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);
        return !claims.getBody().getExpiration().before(new java.util.Date());
    }

    public String getId(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(key.getEncoded()))
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("id")
                .toString();
    }

    public Long getId(HttpServletRequest httpReq) {
        return Long.valueOf(
                getId(
                        resolveToken(httpReq)
                )
        );
    }

    public Authentication getAuthentication(String token) {
        String username = getEmail(token);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    private String getEmail(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    private String getPhoneNumber(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}