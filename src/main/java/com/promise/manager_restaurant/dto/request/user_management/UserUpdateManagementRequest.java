package com.promise.manager_restaurant.dto.request.user_management;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserUpdateManagementRequest {

    String userId;

    MultipartFile imageFile;

    String firstName;

    String lastName;

    String email;

    String phone;

    String password;

}
