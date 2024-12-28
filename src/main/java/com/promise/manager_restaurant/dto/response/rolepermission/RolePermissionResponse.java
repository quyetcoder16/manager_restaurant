package com.promise.manager_restaurant.dto.response.rolepermission;

import com.promise.manager_restaurant.dto.response.permission.PermissionResponse;
import com.promise.manager_restaurant.dto.response.role.RoleResponse;
import com.promise.manager_restaurant.entity.Permission;
import com.promise.manager_restaurant.entity.Role;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RolePermissionResponse {
    RoleResponse role;
    PermissionResponse permission;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
