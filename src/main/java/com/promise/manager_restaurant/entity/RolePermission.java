package com.promise.manager_restaurant.entity;

import com.promise.manager_restaurant.entity.keys.KeyRolePermission;
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


    @EmbeddedId
    KeyRolePermission keyRolePermission;

    @ManyToOne
    @JoinColumn(name = "role_name", insertable = false, updatable = false)
    Role role;

    @ManyToOne
    @JoinColumn(name = "permission_name", insertable = false, updatable = false)
    Permission permission;
}
