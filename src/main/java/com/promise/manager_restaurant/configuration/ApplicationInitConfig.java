package com.promise.manager_restaurant.configuration;

import com.promise.manager_restaurant.entity.User;
import com.promise.manager_restaurant.entity.UserRole;
import com.promise.manager_restaurant.entity.keys.KeyUserRoleId;
import com.promise.manager_restaurant.repository.RoleRepository;
import com.promise.manager_restaurant.repository.UserRepository;
import com.promise.manager_restaurant.repository.UserRoleRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;

@Slf4j
@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ApplicationInitConfig {

    @Autowired
    @Lazy
    private PasswordEncoder passwordEncoder;

    @Bean
    ApplicationRunner applicationRunner(UserRepository userRepository, UserRoleRepository userRoleRepository, RoleRepository roleRepository) {
        return args -> {
            if (userRepository.findByEmail("admin@quyet.com").isEmpty()) {
                // Lấy đối tượng Role
                var adminRole = roleRepository.getRolesByRoleName("ADMIN");
                if (adminRole == null) {
                    throw new RuntimeException("Role ADMIN not exist");
                }

                // Tạo User
                User user = User.builder()
                        .email("admin@quyet.com")
                        .password(passwordEncoder.encode("admin"))
                        .isActive(true)
                        .build();
                userRepository.save(user);

                // Lấy lại User vừa tạo
                var savedUser = userRepository.getUserByEmail("admin@quyet.com");


                // Tạo UserRole
                KeyUserRoleId keyUserRoleId = new KeyUserRoleId(savedUser.getUserId(), adminRole.getRoleName());

                UserRole userRole = UserRole.builder()
                        .keyUserRoleId(keyUserRoleId)
                        .role(adminRole)
                        .user(savedUser)
                        .build();
                userRoleRepository.save(userRole);

                log.warn("Admin user has been created with email: admin@quyet.com and password: admin. Please change it.");
            }
        };
    }

}
