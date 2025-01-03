package com.promise.manager_restaurant.repository;

import com.promise.manager_restaurant.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    boolean existsUserByEmail(String email);

    Optional<User> findByEmail(String email);

    User getUserByEmail(String mail);

    Optional<User> findByUserId(String userId);

    User findByPhone(String phone);

    boolean existsUserByPhone(String phone);
}
