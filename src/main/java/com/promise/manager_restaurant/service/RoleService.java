package com.promise.manager_restaurant.service;

import com.promise.manager_restaurant.dto.request.role.RoleCreationRequest;
import com.promise.manager_restaurant.dto.request.rolepermission.RolePermissionRequest;
import com.promise.manager_restaurant.dto.response.role.RoleResponse;
import com.promise.manager_restaurant.dto.response.rolepermission.RolePermissionResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface RoleService {
    public RoleResponse createRole(RoleCreationRequest roleCreationRequest);
    public List<RoleResponse> getAllRoles();
    public RolePermissionResponse addRolePermission(RolePermissionRequest rolePermissionRequest);
    public void deleteRolePermission(RolePermissionRequest rolePermissionRequest);
    public RoleResponse getRole(String roleName);
}
