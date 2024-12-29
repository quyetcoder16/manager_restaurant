package com.promise.manager_restaurant.service.impl;

import com.promise.manager_restaurant.dto.request.user.ChangePasswordUserRequest;
import com.promise.manager_restaurant.dto.request.user.UserUpdateRequest;
import com.promise.manager_restaurant.dto.response.user.UserResponse;
import com.promise.manager_restaurant.entity.User;
import com.promise.manager_restaurant.exception.AppException;
import com.promise.manager_restaurant.exception.ErrorCode;
import com.promise.manager_restaurant.mapper.UserMapper;
import com.promise.manager_restaurant.repository.UserRepository;
import com.promise.manager_restaurant.service.FilesStorageService;
import com.promise.manager_restaurant.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserServiceImpl implements UserService {

    UserRepository userRepository;
    PasswordEncoder passwordEncoder;
    UserMapper userMapper;
    FilesStorageService filesStorageService;


    @Override
    public UserResponse updateUser(UserUpdateRequest userUpdateRequest) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!authentication.getName().equals(userUpdateRequest.getUserId())) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        User existingUser = userRepository.findByUserId(userUpdateRequest.getUserId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));


        if (userUpdateRequest.getFirstName() != null) {
            existingUser.setFirstName(userUpdateRequest.getFirstName());
        }

        if (userUpdateRequest.getLastName() != null) {
            existingUser.setLastName(userUpdateRequest.getLastName());
        }

        if (userUpdateRequest.getEmail() != null) {
            User checkEmailExist = userRepository.findByEmail(userUpdateRequest.getEmail())
                    .orElse(null);
            if (checkEmailExist != null) {
                if (!existingUser.getUserId().equals(checkEmailExist.getUserId())) {
                    throw new AppException(ErrorCode.EMAIL_EXISTED);
                }
            }
            existingUser.setEmail(userUpdateRequest.getEmail());
        }

        if (userUpdateRequest.getPhone() != null) {
            User checkPhoneExist = userRepository.findByPhone(userUpdateRequest.getPhone());
            if (checkPhoneExist != null) {
                if (!existingUser.getUserId().equals(checkPhoneExist.getUserId())) {
                    throw new AppException(ErrorCode.PHONE_EXISTED);
                }
            }
            existingUser.setPhone(userUpdateRequest.getPhone());
        }

        if (userUpdateRequest.getImageFile() != null) {
            String newAvatar = filesStorageService.saveFile(userUpdateRequest.getImageFile());
            existingUser.setAvatar(newAvatar);
        }

        User updateUser = userRepository.save(existingUser);

        return userMapper.toUserResponse(updateUser);
    }

    @Override
    public void changePassword(ChangePasswordUserRequest changePasswordUserRequest) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!authentication.getName().equals(changePasswordUserRequest.getUserId())) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        var user = userRepository.findByUserId(changePasswordUserRequest.getUserId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        if (!passwordEncoder.matches(changePasswordUserRequest.getOldPassword(), user.getPassword())) {
            throw new AppException(ErrorCode.WRONG_PASSWORD);
        }

        user.setPassword(passwordEncoder.encode(changePasswordUserRequest.getNewPassword()));
        userRepository.save(user);

    }
}
