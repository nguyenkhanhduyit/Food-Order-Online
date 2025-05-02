package com.foodorder.controller;

import com.foodorder.config.JwtUtils;
import com.foodorder.dto.request.LoginRequest;
import com.foodorder.dto.request.UserRequest;
import com.foodorder.dto.response.ApiResponse;
import com.foodorder.service.AuthService;
import com.foodorder.model.CustomUserDetails;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class AuthController {
        /*
        Note : All method tested completed
        */
        AuthService authService;
        AuthenticationManager authenticationManager;
        JwtUtils jwtUtils;

        @PostMapping("/login")
        public ResponseEntity<ApiResponse<String>> login(
                @RequestBody @Valid LoginRequest request,
                HttpServletResponse response) {
                Authentication authentication = authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
                );
                CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
                String jwt = jwtUtils.generateToken(authentication);
                Set<String> authorities = user.getAuthorities()
                        .stream().map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toSet());
                Cookie jwtCookie = new Cookie("authToken", jwt);
                jwtCookie.setHttpOnly(true);
                jwtCookie.setSecure(false);
                jwtCookie.setPath("/");
                jwtCookie.setMaxAge(2 * 60 * 60);
                response.addCookie(jwtCookie);

                String cookieHeader = String.format("authToken=%s; Path=/; HttpOnly; SameSite=Strict", jwt);
                response.setHeader("Set-Cookie", cookieHeader);
                return ResponseEntity.ok().body(
                        ApiResponse.<String>builder().code(200).message("Login successfully").build());
        }

        @PostMapping("/logout")
        public ResponseEntity<ApiResponse<String>> logout(HttpServletResponse response) {
                Cookie cookie = new Cookie("authToken", null);
                cookie.setHttpOnly(true);
                cookie.setPath("/");
                cookie.setMaxAge(0);
                response.addCookie(cookie);
                SecurityContextHolder.getContext().setAuthentication(null);
                return ResponseEntity.ok().body(
                        ApiResponse.<String>builder()
                                .code(200)
                                .message("Logout successfully")
                                .build()
                );
        }

        @GetMapping("/protected")
        public ResponseEntity<ApiResponse<Boolean>> protectedResource(
                @CookieValue(value = "authToken", required = true) String token) {
                if (token != null && jwtUtils.validateToken(token)) {
                        return ResponseEntity.ok(ApiResponse.<Boolean>builder()
                                .code(200)
                                .message(true)
                                .build());
                }
                return ResponseEntity.status(401).body(ApiResponse.<Boolean>builder()
                        .code(401)
                        .message(false)
                        .build());
        }

        @PostMapping("/signup")
        public ResponseEntity<ApiResponse<String>> createUser(@RequestBody @Valid UserRequest request) {
                authService.createUser(request);
                return ResponseEntity.ok(
                        ApiResponse.<String>builder()
                                .code(200)
                                .message("Registration new user successfully")
                                .build());
        }
}