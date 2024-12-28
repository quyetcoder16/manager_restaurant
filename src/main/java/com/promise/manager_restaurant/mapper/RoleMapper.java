package com.promise.manager_restaurant.mapper;

import com.promise.manager_restaurant.dto.request.role.RoleCreationRequest;
import com.promise.manager_restaurant.dto.response.role.RoleResponse;
import com.promise.manager_restaurant.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    Role tRole(RoleCreationRequest roleCreationRequest);

    RoleResponse tRoleResponse(Role role);
}
