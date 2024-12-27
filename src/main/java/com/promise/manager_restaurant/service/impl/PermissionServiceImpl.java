package com.promise.manager_restaurant.service.impl;

import com.promise.manager_restaurant.dto.request.permission.AddPermissionRequest;
import com.promise.manager_restaurant.dto.response.permission.PermissionResponse;
import com.promise.manager_restaurant.entity.Permission;
import com.promise.manager_restaurant.exception.AppException;
import com.promise.manager_restaurant.exception.ErrorCode;
import com.promise.manager_restaurant.mapper.PermissionMapper;
import com.promise.manager_restaurant.repository.PermissionRepository;
import com.promise.manager_restaurant.service.PermissionService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermissionServiceImpl implements PermissionService {

    PermissionRepository permissionRepository;
    PermissionMapper permissionMapper;

    @Override
    @PreAuthorize("hasAuthority('ADD_PERMISSION')")
    public PermissionResponse addPermission(AddPermissionRequest addPermissionRequest) {

        if (permissionRepository.existsPermissionByPermissionName(addPermissionRequest.getPermissionName())) {
            throw new AppException(ErrorCode.PERMISSION_EXISTED);
        }

        Permission permission = permissionMapper.toPermission(addPermissionRequest);

        return permissionMapper.toPermissionResponse(permissionRepository.save(permission));
    }

    @Override
    @PreAuthorize("hasAuthority('READ_PERMISSION')")
    public List<PermissionResponse> getAllPermissions() {
        return permissionRepository.findAll().stream().map(permissionMapper::toPermissionResponse).collect(Collectors.toList());
    }
}
