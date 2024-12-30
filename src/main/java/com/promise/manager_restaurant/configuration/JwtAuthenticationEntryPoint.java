package com.promise.manager_restaurant.configuration;

import com.fasterxml.jackson.databind.ObjectMapper; // Thư viện để chuyển đổi đối tượng Java sang JSON.
import com.promise.manager_restaurant.dto.response.ApiResponse;
import com.promise.manager_restaurant.exception.ErrorCode;
import jakarta.servlet.ServletException; // Ngoại lệ của Java Servlet API.
import jakarta.servlet.http.HttpServletRequest; // Đối tượng đại diện cho yêu cầu HTTP.
import jakarta.servlet.http.HttpServletResponse; // Đối tượng đại diện cho phản hồi HTTP.
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType; // Định nghĩa các kiểu media như JSON, XML.
import org.springframework.security.core.AuthenticationException; // Ngoại lệ khi xác thực thất bại.
import org.springframework.security.web.AuthenticationEntryPoint; // Giao diện xử lý xác thực thất bại.


import java.io.IOException; // Ngoại lệ xử lý I/O.

@Slf4j
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    /**
     * Phương thức này được gọi khi có lỗi xác thực xảy ra, ví dụ: JWT không hợp lệ hoặc thiếu JWT.
     *
     * @param request       Yêu cầu HTTP nhận được từ client.
     * @param response      Phản hồi HTTP được trả về cho client.
     * @param authException Ngoại lệ xác thực JWT, cung cấp thông tin về lỗi.
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        // In thông tin gốc của lỗi
        Throwable rootCause = authException;
        while (rootCause.getCause() != null) {
            rootCause = rootCause.getCause(); // Tìm nguyên nhân gốc
        }

        // Log thông tin nguyên nhân gốc
        log.error("Authentication failed: {}", authException.getMessage());
        log.error("Root cause: {}", rootCause.getMessage(), rootCause);

        // Kiểm tra và xử lý mã lỗi
        ErrorCode errorCode;
        if (rootCause instanceof org.springframework.security.oauth2.jwt.JwtException &&
                rootCause.getMessage().contains("USER_LOCKED")) {
            errorCode = ErrorCode.USER_LOCKED; // Người dùng bị khóa
        } else if (authException.getMessage().contains("INVALID_TOKEN")) {
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
