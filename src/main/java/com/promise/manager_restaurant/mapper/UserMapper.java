package com.promise.manager_restaurant.mapper;

import com.promise.manager_restaurant.dto.request.auth.RegisterRequest;
import com.promise.manager_restaurant.dto.request.user_management.UserCreationRequest;
import com.promise.manager_restaurant.dto.response.auth.RegisterResponse;
import com.promise.manager_restaurant.dto.response.user.UserResponse;
import com.promise.manager_restaurant.dto.response.user_management.UserManagementResponse;
import com.promise.manager_restaurant.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toUsers(UserCreationRequest userCreationRequest);

    UserResponse toUserResponse(User user);

    RegisterResponse toRegisterResponse(User user);

    User toUser(RegisterRequest registerRequest);

    UserManagementResponse toUserManagementResponse(User user);

}
