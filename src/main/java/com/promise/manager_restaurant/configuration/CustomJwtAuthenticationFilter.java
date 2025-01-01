package com.promise.manager_restaurant.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.promise.manager_restaurant.dto.response.ApiResponse;
import com.promise.manager_restaurant.exception.ErrorCode;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CustomJwtAuthenticationFilter extends OncePerRequestFilter {

    CustomJwtDecoder customJwtDecoder;
    AntPathMatcher antPathMatcher = new AntPathMatcher();

    private final String[] PUBLIC_ENDPOINTS = {
            "/api/auth/**",
            "/api/public/**",
    };

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        try {

            if (checkPublicEndpoints(request)) {
                filterChain.doFilter(request, response); // Tiếp tục chuỗi filter nếu là endpoint công khai
                return;
            }

            String authHeader = request.getHeader("Authorization");

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                handleException(response, "UNAUTHORIZED");
                return;
            }

            String token = authHeader.substring(7); // Bỏ "Bearer " để lấy token

            Jwt jwt = customJwtDecoder.decode(token);

            filterChain.doFilter(request, response);
            return;

        } catch (AuthenticationServiceException authenticationServiceException) {
            handleException(response, authenticationServiceException.getMessage());
        }

    }

    private boolean checkPublicEndpoints(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        for (String endpoint : PUBLIC_ENDPOINTS) {
            if (antPathMatcher.match(endpoint, requestURI)) {
                return true; // Là endpoint công khai
            }
        }
        return false;
    }

    private void handleException(HttpServletResponse response, String nameErrorCode) throws IOException {

        // Kiểm tra và xử lý mã lỗi
        ErrorCode errorCode;
        if (nameErrorCode.contains("USER_LOCKED")) {
            errorCode = ErrorCode.USER_LOCKED; // Người dùng bị khóa
        } else if (nameErrorCode.contains("INVALID_TOKEN")) {
            errorCode = ErrorCode.INVALID_TOKEN; // Token không hợp lệ
        } else {
            errorCode = ErrorCode.UNAUTHORIZED; // Mặc định
        }

        // Gửi phản hồi
        response.setStatus(errorCode.getHttpStatus().value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        ApiResponse<?> apiResponse = ApiResponse.builder()
                .code(errorCode.getErrorCode())
                .data(errorCode.getErrorMsg())
                .build();

        ObjectMapper objectMapper = new ObjectMapper();
        response.getWriter().write(objectMapper.writeValueAsString(apiResponse));
        response.flushBuffer();
    }

}
