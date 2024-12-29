package com.promise.manager_restaurant.dto.request.user_management;

import lombok.*;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RemoveRoleOfUserRequest {
    String userId;
    String roleName;
}
