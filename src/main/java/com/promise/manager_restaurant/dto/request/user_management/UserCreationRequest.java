package com.promise.manager_restaurant.dto.request.user_management;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreationRequest {

    String firstName;

    String lastName;

    @Email(message = "invalid email")
    String email;

    String phone;

    String roleName;

    @Size(min = 7, message = "INVALID_PASSWORD")
    String password;
}
