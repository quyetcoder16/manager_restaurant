package com.promise.manager_restaurant.controller;

import com.promise.manager_restaurant.dto.request.user.ChangePasswordUserRequest;
import com.promise.manager_restaurant.dto.request.user.UserCreationRequest;
import com.promise.manager_restaurant.dto.request.user.UserUpdateRequest;
import com.promise.manager_restaurant.dto.response.ApiResponse;
import com.promise.manager_restaurant.dto.response.user.UserResponse;
import com.promise.manager_restaurant.service.FilesStorageService;
import com.promise.manager_restaurant.service.UserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {
    UserService userService;
    FilesStorageService filesStorageService;


    @GetMapping("/file/{filename:.+}")
    public ResponseEntity<?> getFileRestaurant(@PathVariable String filename) {
        Resource resource = filesStorageService.loadFile(filename);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @PostMapping("/update_info")
    public ResponseEntity<ApiResponse<UserResponse>> updateInfo(@ModelAttribute UserUpdateRequest request) {
        return new ResponseEntity<>(ApiResponse.<UserResponse>builder()
                .data(userService.updateUser(request))
                .message("User updated successfully!")
                .build(), HttpStatus.OK);
    }

    @PostMapping("/change_password")
    public ResponseEntity<ApiResponse<Void>> changePassword(@RequestBody @Valid ChangePasswordUserRequest changePasswordUserRequest) {
        userService.changePassword(changePasswordUserRequest);
        return new ResponseEntity<>(ApiResponse.<Void>builder()
                .message("Password changed successfully!")
                .build(), HttpStatus.OK);
    }

}
