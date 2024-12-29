package com.promise.manager_restaurant.controller;

import com.promise.manager_restaurant.dto.request.user_management.*;
import com.promise.manager_restaurant.dto.response.ApiResponse;
import com.promise.manager_restaurant.dto.response.user_management.UserManagementResponse;
import com.promise.manager_restaurant.service.UserManagementService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user_management")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserManagementController {
    UserManagementService userManagementService;

    @PostMapping("/create_user")
    public ResponseEntity<ApiResponse<UserManagementResponse>> createUser(@RequestBody UserCreationRequest userCreationRequest) {
        return new ResponseEntity<>(ApiResponse.<UserManagementResponse>builder()
                .data(userManagementService.createNewUser(userCreationRequest))
                .message("User created successfully!")
                .build(), HttpStatus.OK);
    }

    @PostMapping("/update_info")
    public ResponseEntity<ApiResponse<UserManagementResponse>> updateInfo(@ModelAttribute UserUpdateManagementRequest request) {
        return new ResponseEntity<>(ApiResponse.<UserManagementResponse>builder()
                .data(userManagementService.updateUser(request))
                .message("User updated successfully!")
                .build(), HttpStatus.OK);
    }

    @PostMapping("/change_password")
    public ResponseEntity<ApiResponse<Void>> changePassword(@RequestBody @Valid ChangePasswordUserManagementRequest changePasswordUserManagementRequest) {
        userManagementService.changePassword(changePasswordUserManagementRequest);
        return new ResponseEntity<>(ApiResponse.<Void>builder()
                .message("Password changed successfully!")
                .build(), HttpStatus.OK);
    }

    @PostMapping("/add_role_to_user")
    public ResponseEntity<ApiResponse<Void>> addRoleToUser(@RequestBody AddRoleToUserRequest addRoleToUserRequest) {
        userManagementService.addRoleToUser(addRoleToUserRequest);
        return new ResponseEntity<>(ApiResponse.<Void>builder()
                .message("added role to user successfully!")
                .build(), HttpStatus.OK);
    }

    @DeleteMapping("/remove_role_of_user")
    public ResponseEntity<ApiResponse<Void>> removeRoleOfUser(@RequestBody RemoveRoleOfUserRequest removeRoleOfUserRequest) {
        userManagementService.removeRoleOfUser(removeRoleOfUserRequest);
        return new ResponseEntity<>(ApiResponse.<Void>builder()
                .message("removed role of user successfully!")
                .build(), HttpStatus.OK);
    }

    @PutMapping("/change_status/{userId}")
    public ResponseEntity<ApiResponse<UserManagementResponse>> changeStatusOfUser(@PathVariable("userId") String userId) {
        return new ResponseEntity<>(ApiResponse.<UserManagementResponse>builder()
                .data(userManagementService.changeStatusOfUSer(userId))
                .message("changed status of user successfully!")
                .build(), HttpStatus.OK);
    }

}
