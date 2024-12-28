package com.promise.manager_restaurant.service.impl;

import ch.qos.logback.core.spi.ErrorCodes;
import com.promise.manager_restaurant.dto.request.role.RoleCreationRequest;
import com.promise.manager_restaurant.dto.request.rolepermission.RolePermissionRequest;
import com.promise.manager_restaurant.dto.response.permission.PermissionResponse;
import com.promise.manager_restaurant.dto.response.role.RoleResponse;
import com.promise.manager_restaurant.dto.response.rolepermission.RolePermissionResponse;
import com.promise.manager_restaurant.entity.Permission;
import com.promise.manager_restaurant.entity.Role;
import com.promise.manager_restaurant.entity.RolePermission;
import com.promise.manager_restaurant.entity.keys.KeyRolePermission;
import com.promise.manager_restaurant.exception.AppException;
import com.promise.manager_restaurant.exception.ErrorCode;
import com.promise.manager_restaurant.mapper.PermissionMapper;
import com.promise.manager_restaurant.mapper.RoleMapper;
import com.promise.manager_restaurant.repository.PermissionRepository;
import com.promise.manager_restaurant.repository.RolePermissionRepository;
import com.promise.manager_restaurant.repository.RoleRepository;
import com.promise.manager_restaurant.service.RoleService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleServiceImpl implements RoleService {

    RoleRepository roleRepository;
    RoleMapper roleMapper;
    RolePermissionRepository rolePermissionRepository;
    PermissionRepository permissionRepository;
    PermissionMapper permissionMapper;

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public RoleResponse createRole(RoleCreationRequest roleCreationRequest) {

        if (roleRepository.existsRoleByRoleName(roleCreationRequest.getRoleName())){
            throw new AppException(ErrorCode.ROLE_EXISTED);
        }

        Role role = roleMapper.tRole(roleCreationRequest);

        return roleMapper.tRoleResponse(roleRepository.save(role));
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public List<RoleResponse> getAllRoles() {
        List<Role> roles = roleRepository.findAll();
        List<RoleResponse> roleResponses = new ArrayList<>();
        for (Role role : roles) {
            RoleResponse roleResponse = RoleResponse.builder()
                    .roleName(role.getRoleName())
                    .build();
            List<PermissionResponse> permissionResponses = role.getListRolePermission().stream()
                    .map(rolePermission -> {
                        Permission permission = rolePermission.getPermission();
                        return PermissionResponse.builder()
                                .permissionName(permission.getPermissionName())
                                .description(permission.getDescription())
                                .build();
                    })
                    .collect(Collectors.toList());
            roleResponse.setPermissionResponses(permissionResponses);
            roleResponses.add(roleResponse);
        }
        return roleResponses;
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public RolePermissionResponse addRolePermission(RolePermissionRequest rolePermissionRequest) {
        var roleName = rolePermissionRequest.getRoleName();
        var permissionName = rolePermissionRequest.getPermissionName();

        if (!roleRepository.existsRoleByRoleName(roleName)){
            throw new AppException(ErrorCode.ROLE_NOT_EXISTED);
        }

        if (!permissionRepository.existsPermissionByPermissionName(permissionName)){
            throw new AppException(ErrorCode.PERMISSION_NOT_EXISTED);
        }

        Role role = roleRepository.getRolesByRoleName(roleName);
        Permission permission = permissionRepository.getPermissionByPermissionName(permissionName);

        KeyRolePermission keyRolePermission = KeyRolePermission.builder()
                .permissionName(permissionName)
                .roleName(roleName)
                .build();

        if (rolePermissionRepository.existsRolePermissionByKeyRolePermission(keyRolePermission)){
            throw new AppException(ErrorCode.ROLE_PERMISSON_EXISTED);
        }

        RolePermission rolePermission = RolePermission.builder()
                .keyRolePermission(keyRolePermission)
                .role(role)
                .permission(permission)
                .build();

        role.getListRolePermission().add(rolePermission);

        LocalDateTime createdAt = rolePermissionRepository.save(rolePermission).getCreatedAt();
        roleRepository.save(role);

        return RolePermissionResponse.builder()
                .role(roleMapper.tRoleResponse(role))
                .permission(permissionMapper.toPermissionResponse(permission))
                .createdAt(createdAt)
                .updatedAt(createdAt)
                .build();
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteRolePermission(RolePermissionRequest rolePermissionRequest) {
        var roleName = rolePermissionRequest.getRoleName();
        var permissionName = rolePermissionRequest.getPermissionName();

        if (!roleRepository.existsRoleByRoleName(roleName)){
            throw new AppException(ErrorCode.ROLE_NOT_EXISTED);
        }

        if (!permissionRepository.existsPermissionByPermissionName(permissionName)){
            throw new AppException(ErrorCode.PERMISSION_NOT_EXISTED);
        }

        Role role = roleRepository.getRolesByRoleName(roleName);
        Permission permission = permissionRepository.getPermissionByPermissionName(permissionName);

        KeyRolePermission keyRolePermission = KeyRolePermission.builder()
                .permissionName(permissionName)
                .roleName(roleName)
                .build();

        if (!rolePermissionRepository.existsRolePermissionByKeyRolePermission(keyRolePermission)){
            throw new AppException(ErrorCode.ROLE_PERMISSION_NOT_EXISTED);
        }
        rolePermissionRepository.deleteById(keyRolePermission);

        role.getListRolePermission().remove(keyRolePermission);
        roleRepository.save(role);
        permission.getListRolePermission().remove(keyRolePermission);
        permissionRepository.save(permission);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public RoleResponse getRole(String roleName) {

        Role role = roleRepository.findByRoleNameIgnoreCase(roleName).orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_EXISTED));

        RoleResponse roleResponse = roleMapper.tRoleResponse(role);

        List<PermissionResponse> permissionResponses = role.getListRolePermission().stream()
                .map(rolePermission -> {
                    Permission permission = rolePermission.getPermission();
                    return PermissionResponse.builder()
                            .permissionName(permission.getPermissionName())
                            .description(permission.getDescription())
                            .build();
                })
                .collect(Collectors.toList());
        roleResponse.setPermissionResponses(permissionResponses);

        return roleResponse;
    }
}
