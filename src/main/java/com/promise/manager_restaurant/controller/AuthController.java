package com.promise.manager_restaurant.controller;

import com.promise.manager_restaurant.dto.request.auth.RegisterRequest;
import com.promise.manager_restaurant.dto.request.user.UserCreationRequest;
import com.promise.manager_restaurant.dto.response.ApiResponse;
import com.promise.manager_restaurant.dto.response.auth.RegisterResponse;
import com.promise.manager_restaurant.dto.response.user.UserResponse;
import com.promise.manager_restaurant.service.AuthService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
