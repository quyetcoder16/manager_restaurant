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
        System.out.println("----------------------JwtAuthenticationEntryPoint: " + authException.getMessage());

        log.error("Authentication failed: {}", authException.getMessage());

        // Xác định mã lỗi cụ thể dựa trên thông điệp ngoại lệ
        ErrorCode errorCode;
        Throwable rootCause = authException.getCause(); // Lấy nguyên nhân gốc (nếu có)

        if (rootCause instanceof org.springframework.security.authentication.AuthenticationServiceException &&
                rootCause.getMessage().contains("UNAUTHENTICATED")) {
            errorCode = ErrorCode.UNAUTHENTICATED; // Lỗi xác thực
        } else if (authException.getMessage().contains("INVALID_TOKEN")) {
            errorCode = ErrorCode.INVALID_TOKEN; // Token không hợp lệ
        } else if (authException.getMessage().contains("USER_LOCKED")) {
            errorCode = ErrorCode.USER_LOCKED; // Người dùng bị khóa
        } else {
            errorCode = ErrorCode.UNAUTHORIZED; // Mặc định nếu không khớp
        }

        // Thiết lập phản hồi HTTP
        response.setStatus(errorCode.getHttpStatus().value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        // Tạo đối tượng phản hồi API chứa thông tin lỗi
        ApiResponse<?> apiResponse = ApiResponse.builder()
                .code(errorCode.getErrorCode()) // Mã lỗi từ ErrorCode
                .data(errorCode.getErrorMsg()) // Thông báo lỗi từ ErrorCode
                .build();

        // Chuyển đổi đối tượng phản hồi thành JSON và gửi về client
        ObjectMapper objectMapper = new ObjectMapper();
        response.getWriter().write(objectMapper.writeValueAsString(apiResponse));
        response.flushBuffer();
    }


}
