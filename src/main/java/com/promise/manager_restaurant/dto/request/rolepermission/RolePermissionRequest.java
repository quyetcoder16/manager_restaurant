package com.promise.manager_restaurant.dto.request.rolepermission;

import lombok.*;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RolePermissionRequest {
    String roleName;
    String permissionName;
}
