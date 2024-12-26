package com.promise.manager_restaurant.controller;

import com.promise.manager_restaurant.dto.request.permission.AddPermissionRequest;
import com.promise.manager_restaurant.dto.response.ApiResponse;
import com.promise.manager_restaurant.dto.response.permission.PermissionResponse;
import com.promise.manager_restaurant.service.PermissionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/permission")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermissionController {

    PermissionService permissionService;

    @PostMapping("/add_permission")
    ApiResponse<PermissionResponse> createPermission(@RequestBody  AddPermissionRequest request) {
        return ApiResponse.<PermissionResponse>builder()
                .data(permissionService.addPermission(request))
                .build();
    }


}
