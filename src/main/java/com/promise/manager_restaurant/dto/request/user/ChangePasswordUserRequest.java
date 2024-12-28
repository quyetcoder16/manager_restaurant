package com.promise.manager_restaurant.dto.request.user;

import lombok.*;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChangePasswordUserRequest {
    String userId;
    String oldPassword;
    String newPassword;
}
