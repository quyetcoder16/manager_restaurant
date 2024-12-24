package com.promise.manager_restaurant.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "role_permission")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RolePermission extends BaseEntity {
    @Id
    @Column(name = "role_name")
    String roleName;

    @Id
    @Column(name = "permission_name")
    String permissionName;

    @ManyToOne
    @JoinColumn(name = "role_name")
    Role role;

    @ManyToOne
    @JoinColumn(name = "permission_name")
    Permission permission;
}
