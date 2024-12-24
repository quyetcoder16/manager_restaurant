package com.promise.manager_restaurant.dto.response.user;

import jakarta.persistence.Column;
import lombok.*;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {
    String userId;

    String firstName;

    String lastName;

    String email;

    String phone;
}
