package com.promise.manager_restaurant.repository;

import com.promise.manager_restaurant.entity.UserRole;
import com.promise.manager_restaurant.entity.keys.KeyUserRoleId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, KeyUserRoleId> {
}
