package com.promise.manager_restaurant.service;

import com.promise.manager_restaurant.dto.request.auth.RegisterRequest;
import com.promise.manager_restaurant.dto.response.auth.RegisterResponse;
import org.springframework.stereotype.Service;

@Service
public interface AuthService {
    public RegisterResponse register(RegisterRequest registerRequest);
}
