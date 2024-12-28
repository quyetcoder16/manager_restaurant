package com.promise.manager_restaurant.repository;

import com.promise.manager_restaurant.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, String> {
    boolean existsPermissionByPermissionName(String permissionName);
    Permission getPermissionByPermissionName(String permissionName);
}
