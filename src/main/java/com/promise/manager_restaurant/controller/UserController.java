package com.promise.manager_restaurant.controller;

import com.promise.manager_restaurant.dto.request.user.UserCreationRequest;
import com.promise.manager_restaurant.dto.response.ApiResponse;
import com.promise.manager_restaurant.dto.response.user.UserResponse;
import com.promise.manager_restaurant.service.UserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {
    UserService userService;

    @PostMapping
    ApiResponse<UserResponse> createUser(@RequestBody @Valid UserCreationRequest request) {

        return ApiResponse.<UserResponse>builder()
                .data(userService.createUser(request))
                .build();
    }
}
