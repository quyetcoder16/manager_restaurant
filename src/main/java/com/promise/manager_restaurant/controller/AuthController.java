package com.promise.manager_restaurant.controller;

import com.nimbusds.jose.JOSEException;
import com.promise.manager_restaurant.dto.request.auth.AuthenticationRequest;
import com.promise.manager_restaurant.dto.request.auth.LogoutRequest;
import com.promise.manager_restaurant.dto.request.auth.RefreshRequest;
import com.promise.manager_restaurant.dto.request.auth.RegisterRequest;
import com.promise.manager_restaurant.dto.response.ApiResponse;
import com.promise.manager_restaurant.dto.response.auth.AuthenticationResponse;
import com.promise.manager_restaurant.dto.response.auth.RegisterResponse;
import com.promise.manager_restaurant.service.AuthService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthController {

    AuthService authService;

    @PostMapping("/register")
    ApiResponse<RegisterResponse> createUser(@RequestBody @Valid RegisterRequest request) {

        return ApiResponse.<RegisterResponse>builder()
                .data(authService.register(request))
                .build();
    }

    @PostMapping("/login")
    ApiResponse<AuthenticationResponse> login(@RequestBody @Valid AuthenticationRequest request) {
        return ApiResponse.<AuthenticationResponse>builder()
                .data(authService.login(request))
                .build();
    }

    @PostMapping("/refresh_token")
    ApiResponse<AuthenticationResponse> refreshToken(@RequestBody @Valid RefreshRequest request) throws ParseException, JOSEException {
        return ApiResponse.<AuthenticationResponse>builder()
                .data(authService.refreshToken(request))
                .build();
    }

    @PostMapping("/logout")
    ApiResponse<Void> logout(@RequestBody @Valid LogoutRequest request) throws ParseException, JOSEException {
        authService.logout(request);
        return ApiResponse.<Void>builder().build();
    }
}
