package com.promise.manager_restaurant.service;

import com.promise.manager_restaurant.dto.request.user_management.*;
import com.promise.manager_restaurant.dto.response.user_management.UserManagementResponse;
import org.springframework.stereotype.Service;

@Service
public interface UserManagementService {
    UserManagementResponse createNewUser(UserCreationRequest userCreationRequest);

    UserManagementResponse updateUser(UserUpdateManagementRequest userUpdateManagementRequest);

    void changePassword(ChangePasswordUserManagementRequest changePasswordUserManagementRequest);

    void addRoleToUser(AddRoleToUserRequest addRoleToUserRequest);

    void removeRoleOfUser(RemoveRoleOfUserRequest removeRoleOfUserRequest);

    UserManagementResponse changeStatusOfUSer(String userId);
}
