package com.promise.manager_restaurant.dto.response.permission;


import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PermissionResponse {
    String permissionName;

    String description;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
