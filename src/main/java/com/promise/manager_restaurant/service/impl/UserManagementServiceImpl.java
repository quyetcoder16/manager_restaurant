package com.promise.manager_restaurant.service.impl;

import com.promise.manager_restaurant.dto.request.user.UserUpdateRequest;
import com.promise.manager_restaurant.dto.request.user_management.*;
import com.promise.manager_restaurant.dto.response.user.UserResponse;
import com.promise.manager_restaurant.dto.response.user_management.UserManagementResponse;
import com.promise.manager_restaurant.entity.Role;
import com.promise.manager_restaurant.entity.User;
import com.promise.manager_restaurant.entity.UserRole;
import com.promise.manager_restaurant.entity.keys.KeyUserRoleId;
import com.promise.manager_restaurant.exception.AppException;
import com.promise.manager_restaurant.exception.ErrorCode;
import com.promise.manager_restaurant.mapper.UserMapper;
import com.promise.manager_restaurant.repository.RoleRepository;
import com.promise.manager_restaurant.repository.UserRepository;
import com.promise.manager_restaurant.repository.UserRoleRepository;
import com.promise.manager_restaurant.service.FilesStorageService;
import com.promise.manager_restaurant.service.UserManagementService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserManagementServiceImpl implements UserManagementService {

    UserRepository userRepository;
    UserMapper userMapper;
    RoleRepository roleRepository;
    PasswordEncoder passwordEncoder;
    UserRoleRepository userRoleRepository;
    FilesStorageService filesStorageService;
    UserRoleRepository userUserRoleRepository;

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public UserManagementResponse createNewUser(UserCreationRequest userCreationRequest) {

        if (userRepository.existsUserByEmail(userCreationRequest.getEmail())) {
            throw new AppException(ErrorCode.EMAIL_EXISTED);
        }

        if (userRepository.existsUserByPhone(userCreationRequest.getPhone())) {
            throw new AppException(ErrorCode.PHONE_EXISTED);
        }


        Role role = roleRepository.findRoleByRoleName(userCreationRequest.getRoleName())
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_EXISTED));

        User user = userMapper.toUsers(userCreationRequest);
        user.setIsActive(true);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        User savedUser = userRepository.save(user);

        KeyUserRoleId keyUserRoleId = new KeyUserRoleId();
        keyUserRoleId.setUserId(savedUser.getUserId());
        keyUserRoleId.setRoleName(role.getRoleName());

        UserRole userRole = UserRole.builder()
                .keyUserRoleId(keyUserRoleId)
                .build();

        userRoleRepository.save(userRole);

        return userMapper.toUserManagementResponse(savedUser);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public UserManagementResponse   updateUser(UserUpdateManagementRequest userUpdateRequest) {

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

        if (userUpdateRequest.getPassword() != null) {
            existingUser.setPassword(passwordEncoder.encode(userUpdateRequest.getPassword()));
        }

        if (userUpdateRequest.getImageFile() != null) {
            String newAvatar = filesStorageService.saveFile(userUpdateRequest.getImageFile());
            existingUser.setAvatar(newAvatar);
        }

        User updateUser = userRepository.save(existingUser);

        return userMapper.toUserManagementResponse(updateUser);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public void changePassword(ChangePasswordUserManagementRequest changePasswordUserManagementRequest) {
        var user = userRepository.findByUserId(changePasswordUserManagementRequest.getUserId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));


        user.setPassword(passwordEncoder.encode(changePasswordUserManagementRequest.getNewPassword()));

        userRepository.save(user);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public void addRoleToUser(AddRoleToUserRequest addRoleToUserRequest) {

        userRepository.findByUserId(addRoleToUserRequest.getUserId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        roleRepository.findRoleByRoleName(addRoleToUserRequest.getRoleName()).orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_EXISTED));

        KeyUserRoleId keyUserRoleId = new KeyUserRoleId();
        keyUserRoleId.setUserId(addRoleToUserRequest.getUserId());
        keyUserRoleId.setRoleName(addRoleToUserRequest.getRoleName());

        if (userRoleRepository.existsById(keyUserRoleId)) {
            throw new AppException(ErrorCode.ROLE_EXISTED);
        }

        userRoleRepository.save(
                UserRole.builder()
                        .keyUserRoleId(keyUserRoleId)
                        .build()
        );
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public void removeRoleOfUser(RemoveRoleOfUserRequest removeRoleOfUserRequest) {
        userRepository.findByUserId(removeRoleOfUserRequest.getUserId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        roleRepository.findRoleByRoleName(removeRoleOfUserRequest.getRoleName()).orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_EXISTED));

        KeyUserRoleId keyUserRoleId = new KeyUserRoleId();
        keyUserRoleId.setUserId(removeRoleOfUserRequest.getUserId());
        keyUserRoleId.setRoleName(removeRoleOfUserRequest.getRoleName());

        if (!userRoleRepository.existsById(keyUserRoleId)) {
            throw new AppException(ErrorCode.USER_DONT_HAVE_ROLE);
        }

        userRoleRepository.deleteById(keyUserRoleId);

    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public UserManagementResponse changeStatusOfUSer(String userId) {

        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        user.setIsActive(!user.getIsActive());

        return userMapper.toUserManagementResponse(userRepository.save(user));
    }

}
