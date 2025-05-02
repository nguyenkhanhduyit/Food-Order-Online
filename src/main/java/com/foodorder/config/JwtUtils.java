package com.foodorder.config;

import com.foodorder.model.CustomUserDetails;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtUtils {

    private String jwtSecret = "Rdi+Q1fVKaUXEBGTozXqdyGyuqlzNakokAufGCftoy8CWOCnOONYPqCoJa7NMJ0Z";

    private int expireTime = 323434349;

    private SecretKey key() {
        return Keys.hmacShaKeyFor(this.jwtSecret.getBytes());
    }

    public String generateToken(Authentication authentication) {
        CustomUserDetails userPrincipal = (CustomUserDetails) authentication.getPrincipal();
        Set<String> roles = userPrincipal.getAuthorities()
                .stream().map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());
        return Jwts.builder()
                .subject(userPrincipal.getUsername())
                .claim("roles", roles)
                .issuedAt(new Date())
                .expiration(new Date((new Date()).getTime() + expireTime))
                .signWith(key())
                .compact();
    }

    public String retrieveEmailFromToken(String requestToken) {
        try {
            JwtParser jwtParser = Jwts.parser().verifyWith(key()).build();
            Claims claims = jwtParser.parseSignedClaims(requestToken).getPayload();
            return claims.getSubject();
        } catch (Exception e) {
            log.error("Error retrieving email from token: {}", e.getMessage());
            throw e;
        }
    }

    public boolean validateToken(String token) {
        if (token == null || token.trim().isEmpty()) {
            log.error("Token is null or empty");
            return false;
        }
        try {
            Jwts.parser().verifyWith(key()).build().parse(token);
            return true;
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
            return false;
        } catch (ExpiredJwtException e) {
            log.error("JWT token expired: {}", e.getMessage());
            return false;
        } catch (UnsupportedJwtException e) {
            log.error("JWT token unsupported: {}", e.getMessage());
            return false;
        } catch (IllegalArgumentException e) {
            log.error("Illegal Argument token: {}", e.getMessage());
            return false;
        } catch (Exception e) {
            log.error("Exception in token validation: {}", e.getMessage());
            return false;
        }
    }
}