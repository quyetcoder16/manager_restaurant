package com.promise.manager_restaurant.dto.response.auth;


import lombok.*;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RegisterResponse {
    String userId;

    String firstName;

    String lastName;

    String email;

    String phone;
}
