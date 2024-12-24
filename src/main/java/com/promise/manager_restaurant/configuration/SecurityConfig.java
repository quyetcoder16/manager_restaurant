package com.promise.manager_restaurant.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final String[] PUBLIC_ENDPOINTS = {"/auth/register","/auth/token", "/auth/introspect", "/users", "/auth/logout", "/auth/refresh"};

    /**
     * Cấu hình chuỗi filter bảo mật.
     *
     * @param httpSecurity đối tượng HttpSecurity để cấu hình bảo mật.
     * @return SecurityFilterChain đối tượng chuỗi filter bảo mật.
     * @throws Exception nếu xảy ra lỗi khi cấu hình.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .authorizeHttpRequests(request ->
                        request.requestMatchers(HttpMethod.POST, PUBLIC_ENDPOINTS).permitAll() // Cho phép truy cập các endpoint công khai với POST.
                                // .requestMatchers(HttpMethod.GET, "/users").hasRole(Role.ADMIN.name()) // Chỉ admin được phép GET /users.
                                .anyRequest().authenticated() // Yêu cầu xác thực cho các endpoint khác.
                )
//                .oauth2ResourceServer(oauth2 ->
//                        oauth2.jwt(jwtConfigurer ->
//                                        jwtConfigurer.decoder(customJwtDecoder) // Sử dụng jwtDecoder để giải mã JWT.
//                                                .jwtAuthenticationConverter(jwtAuthenticationConverter())) // Cấu hình ánh xạ quyền từ token.
//                                .authenticationEntryPoint(new JwtAuthenticationEntryPoint()) // Xử lý lỗi xác thực JWT.
//                )
                .csrf(AbstractHttpConfigurer::disable); // Vô hiệu hóa CSRF (phù hợp với API REST).

        return httpSecurity.build(); // Trả về chuỗi filter bảo mật đã cấu hình.
    }

    /**
     * Cấu hình mã hóa mật khẩu bằng BCrypt.
     *
     * @return PasswordEncoder đối tượng mã hóa mật khẩu.
     */
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10); // Mã hóa mật khẩu với độ mạnh 10.
    }
}
