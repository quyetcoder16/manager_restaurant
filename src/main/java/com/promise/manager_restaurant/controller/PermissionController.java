package com.promise.manager_restaurant.controller;

import com.promise.manager_restaurant.dto.request.permission.AddPermissionRequest;
import com.promise.manager_restaurant.dto.response.ApiResponse;
import com.promise.manager_restaurant.dto.response.permission.PermissionResponse;
import com.promise.manager_restaurant.service.PermissionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/permissions")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermissionController {

    PermissionService permissionService;

    @PostMapping
    ApiResponse<PermissionResponse> createPermission(@RequestBody AddPermissionRequest request) {
        return ApiResponse.<PermissionResponse>builder()
                .data(permissionService.addPermission(request))
                .build();
    }

    @GetMapping
    ApiResponse<List<PermissionResponse>> getPermissions() {
        return ApiResponse.<List<PermissionResponse>>builder()
                .data(permissionService.getAllPermissions())
                .build();
    }


}
