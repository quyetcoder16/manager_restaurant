package com.promise.manager_restaurant.service;

import com.promise.manager_restaurant.dto.request.user.UserCreationRequest;
import com.promise.manager_restaurant.dto.response.user.UserResponse;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    public UserResponse createUser(UserCreationRequest user);
}
