package com.promise.manager_restaurant.repository;

import com.promise.manager_restaurant.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {
    Role getRolesByRoleName(String s);
}
