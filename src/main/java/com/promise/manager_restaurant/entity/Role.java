package com.promise.manager_restaurant.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Entity
@Table(name = "role")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Role extends BaseEntity {
    @Id
    @Column(name = "role_name", length = 100)
    String roleName;

    @Column(name = "description", length = 1000)
    String description;

    @OneToMany(mappedBy = "role")
    List<UserRole> listUserRole;

    @OneToMany(mappedBy = "role")
    @ToString.Exclude //// Bỏ qua để tránh vòng lặp
    List<RolePermission> listRolePermission;


}
