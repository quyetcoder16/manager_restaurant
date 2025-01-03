package com.promise.manager_restaurant.dto.response.permission;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)

public class PermissionResponse {
    String permissionName;

    String description;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
