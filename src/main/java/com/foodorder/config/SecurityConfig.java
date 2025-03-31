package com.foodorder.config;

import com.foodorder.service.CustomUserDetailsService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity(securedEnabled = true,jsr250Enabled = true,prePostEnabled = true)
@FieldDefaults(makeFinal = true,level = AccessLevel.PRIVATE)
public class SecurityConfig {

    CustomUserDetailsService userDetailsService;
    JwtUtils jwtUtils;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.sessionManagement(
manage -> manage.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
.exceptionHandling(
exception -> exception.authenticationEntryPoint(new JwtAuthenticationEntryPoint()))
.authorizeHttpRequests(
authorize ->
authorize.requestMatchers("/api/**").hasAnyRole("RESTAURANT_OWNER","ADMIN")
         .requestMatchers("/auth/**","/user/**","/admin/**","/restaurant/**").permitAll()
         .anyRequest().authenticated()
                    )
                .addFilterBefore(rateLimitPerMinuteFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(authenticationTokenFilter(), UsernamePasswordAuthenticationFilter.class)
                .authenticationProvider(daoAuthenticationProvider())
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()));
        return httpSecurity.build();
    }
    private CorsConfigurationSource corsConfigurationSource(){
        return new CorsConfigurationSource() {
            @Override
            public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                CorsConfiguration corsConfiguration = new CorsConfiguration();
                corsConfiguration.setAllowedOrigins(Arrays.asList(
                        "http://localhost:8080","http://localhost:5173"
                ));
                corsConfiguration.setAllowedMethods(Collections.singletonList("*"));
                corsConfiguration.setAllowCredentials(true);
                corsConfiguration.setAllowedHeaders(Collections.singletonList("*"));
                corsConfiguration.setExposedHeaders(Arrays.asList("Authorization"));
                corsConfiguration.setMaxAge(3600L);
                return corsConfiguration;
            }
        };
    }
    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        //chức năng : kiểm tra thông tin người dùng bằng cách lấy dữ liệu từ database.
        // thành phần chịu trách nhiệm xác thực thông tin đăng nhập của người dùng.
        var daoAuthenticationProvider = new DaoAuthenticationProvider();
        /* Nó sử dụng userDetailsService
        để lấy thông tin người dùng từ database.*/
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        // Đặt bộ mã hóa mật khẩu để kiểm tra mật khẩu khi đăng nhập
        //Nó kiểm tra mật khẩu người dùng bằng cách sử dụng PasswordEncoder
        daoAuthenticationProvider.setPasswordEncoder(this.passwordEncoder());
        return daoAuthenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
       //chức năng : quản lý quá trình xác thực và gọi DaoAuthenticationProvider để xử lý yêu cầu đăng nhập.
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthTokenFilter authenticationTokenFilter(){
        return new AuthTokenFilter(jwtUtils,userDetailsService);
    }

    @Bean
    public RateLimitPerMinuteFilter rateLimitPerMinuteFilter() {
        return new RateLimitPerMinuteFilter();
    }
}
