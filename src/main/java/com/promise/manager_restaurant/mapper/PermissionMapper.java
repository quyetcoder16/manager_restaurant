package com.promise.manager_restaurant.mapper;

import com.promise.manager_restaurant.dto.request.permission.AddPermissionRequest;
import com.promise.manager_restaurant.dto.response.permission.PermissionResponse;
import com.promise.manager_restaurant.entity.Permission;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    Permission toPermission(AddPermissionRequest permissionRequest);

    PermissionResponse toPermissionResponse(Permission permission);
}
