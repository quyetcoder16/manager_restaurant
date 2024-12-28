package com.promise.manager_restaurant.controller;

import com.promise.manager_restaurant.dto.request.permission.AddPermissionRequest;
import com.promise.manager_restaurant.dto.request.role.RoleCreationRequest;
import com.promise.manager_restaurant.dto.request.rolepermission.RolePermissionRequest;
import com.promise.manager_restaurant.dto.response.ApiResponse;
import com.promise.manager_restaurant.dto.response.permission.PermissionResponse;
import com.promise.manager_restaurant.dto.response.role.RoleResponse;
import com.promise.manager_restaurant.dto.response.rolepermission.RolePermissionResponse;
import com.promise.manager_restaurant.service.PermissionService;
import com.promise.manager_restaurant.service.RoleService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleController {

    RoleService roleService;

    @PostMapping
    ApiResponse<RoleResponse> createRole(@RequestBody RoleCreationRequest request) {
        return ApiResponse.<RoleResponse>builder()
                .data(roleService.createRole(request))
                .build();
    }

    @GetMapping
    ApiResponse<List<RoleResponse>> getAllRoles() {
        return ApiResponse.<List<RoleResponse>>builder()
                .data(roleService.getAllRoles())
                .build();
    }

    @GetMapping("/{roleName}")
    ApiResponse<RoleResponse> getRoleByRoleName(@PathVariable("roleName") String roleName) {
        return ApiResponse.<RoleResponse>builder()
                .data(roleService.getRole(roleName))
                .build();
    }

    @PostMapping("/permissions")
    ApiResponse<RolePermissionResponse> addPermissionRole(@RequestBody RolePermissionRequest request) {
        return ApiResponse.<RolePermissionResponse>builder()
                .data(roleService.addRolePermission(request))
                .build();
    }

    @DeleteMapping("/permissions")
    ApiResponse<Void>deletePermissionRole(@RequestBody RolePermissionRequest request) {
        roleService.deleteRolePermission(request);
        return ApiResponse.<Void>builder()
                .message("Remove permission successfully")
                .build();
    }
}
