package com.promise.manager_restaurant.service.impl;

import com.promise.manager_restaurant.dto.request.auth.RegisterRequest;
import com.promise.manager_restaurant.dto.response.auth.RegisterResponse;
import com.promise.manager_restaurant.entity.User;
import com.promise.manager_restaurant.exception.AppException;
import com.promise.manager_restaurant.exception.ErrorCode;
import com.promise.manager_restaurant.mapper.UserMapper;
import com.promise.manager_restaurant.repository.UserRepository;
import com.promise.manager_restaurant.service.AuthService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthServiceImpl implements AuthService {

    UserRepository userRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;

    @Override
    public RegisterResponse register(RegisterRequest registerRequest) {
        if (userRepository.existsUserByEmail(registerRequest.getEmail())) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }

        User user = userMapper.toUser(registerRequest);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setIsActive(true);

        return userMapper.toRegisterResponse(userRepository.save(user));
    }
}
