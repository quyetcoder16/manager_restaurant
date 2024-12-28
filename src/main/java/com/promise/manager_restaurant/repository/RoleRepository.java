package com.promise.manager_restaurant.repository;

import com.promise.manager_restaurant.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {
    Role getRolesByRoleName(String roleName);

    boolean existsRoleByRoleName(String roleName);

    Optional<Role> findByRoleNameIgnoreCase(String roleName); //tìm kiếm không phân biejet in hoa in thường

    Optional<Role> findRoleByRoleName(String roleName);
}
