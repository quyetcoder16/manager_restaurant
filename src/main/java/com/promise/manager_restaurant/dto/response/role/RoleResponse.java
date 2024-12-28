package com.promise.manager_restaurant.dto.response.role;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.promise.manager_restaurant.dto.response.permission.PermissionResponse;
import com.promise.manager_restaurant.dto.response.rolepermission.RolePermissionResponse;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RoleResponse {

    String roleName;

    String description;

    List<PermissionResponse> permissionResponses;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
