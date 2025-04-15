package com.foodorder.config;

import com.foodorder.exception.declare.ResourceNotAvailableException;
import com.foodorder.service.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class AuthTokenFilter extends OncePerRequestFilter {

    JwtUtils jwtUtils;
    CustomUserDetailsService userDetailsService;

    private String extractJwtFromCookie(HttpServletRequest request) {
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (cookie.getName().equals("authToken") && !cookie.getValue().isBlank()) {
                    log.info("Found token in cookie: {}", cookie.getValue());
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,@NonNull HttpServletResponse response,@NonNull FilterChain filterChain)
            throws ServletException, IOException {
        String jwt = extractJwtFromCookie(request);
        log.info("Extracted JWT for validation: {}", jwt);
        if (jwt != null && !jwt.isBlank() && jwtUtils.validateToken(jwt)) {
            try {
                String username = jwtUtils.retrieveEmailFromToken(jwt);
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (UsernameNotFoundException e) {
                log.error("User not found: {}", e.getMessage());
                throw new ResourceNotAvailableException(e.getMessage());
            } catch (Exception e) {
                log.error("Error in filter: {}", e.getMessage());
                throw new RuntimeException(e);
            }
        } else {
            log.error("JWT Token is invalid or not found");
        }

        filterChain.doFilter(request, response);
    }
}