package com.promise.manager_restaurant.service;

import com.promise.manager_restaurant.dto.request.permission.AddPermissionRequest;
import com.promise.manager_restaurant.dto.response.permission.PermissionResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PermissionService {
    PermissionResponse addPermission(AddPermissionRequest addPermissionRequest);

    List<PermissionResponse> getAllPermissions();
}
