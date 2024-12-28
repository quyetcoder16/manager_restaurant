package com.promise.manager_restaurant.repository;

import com.promise.manager_restaurant.entity.RolePermission;
import com.promise.manager_restaurant.entity.keys.KeyRolePermission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RolePermissionRepository extends JpaRepository<RolePermission, KeyRolePermission> {
    boolean existsRolePermissionByKeyRolePermission(KeyRolePermission keyRolePermission);
}
