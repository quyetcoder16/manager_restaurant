package com.promise.manager_restaurant.dto.request.permission;


import lombok.*;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AddPermissionRequest {

    String permissionName;

    String description;
}
