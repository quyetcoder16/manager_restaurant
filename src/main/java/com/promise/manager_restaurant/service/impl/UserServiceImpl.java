package com.promise.manager_restaurant.service.impl;

import com.promise.manager_restaurant.dto.request.user.UserCreationRequest;
import com.promise.manager_restaurant.dto.response.user.UserResponse;
import com.promise.manager_restaurant.entity.User;
import com.promise.manager_restaurant.exception.AppException;
import com.promise.manager_restaurant.exception.ErrorCode;
import com.promise.manager_restaurant.mapper.UserMapper;
import com.promise.manager_restaurant.repository.UserRepository;
import com.promise.manager_restaurant.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserServiceImpl implements UserService {

    UserRepository userRepository;
    PasswordEncoder passwordEncoder;
    UserMapper userMapper;


    @Override
    public UserResponse createUser(UserCreationRequest userRequest) {
        if (userRepository.existsUserByEmail(userRequest.getEmail())) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }

        User user = userMapper.toUsers(userRequest);
        user.setIsActive(true);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userMapper.toUserResponse(userRepository.save(user));


    }
}
