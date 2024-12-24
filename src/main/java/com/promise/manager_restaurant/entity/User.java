package com.promise.manager_restaurant.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Entity
@Table(name = "user")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "user_id")
    String userId;

    @Column(name = "first_name", length = 100)
    String firstName;

    @Column(name = "last_name", length = 100)
    String lastName;

    @Column(name = "email", nullable = false)
    String email;

    @Column(name = "phone")
    String phone;

    @Column(name = "password", nullable = false)
    String password;


    @Column(name = "is_active", columnDefinition = "BOOLEAN DEFAULT true")
    Boolean isActive = true;

    @OneToMany(mappedBy = "user")
    List<UserRole> listUserRole;

}
