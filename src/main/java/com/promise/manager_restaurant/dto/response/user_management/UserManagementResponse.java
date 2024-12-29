package com.promise.manager_restaurant.dto.response.user_management;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserManagementResponse {
    String userId;

    String firstName;

    String lastName;

    String email;

    String phone;

    String avatar;

    boolean isActive;

}
