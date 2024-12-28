package com.promise.manager_restaurant.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Table(name = "permission")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Permission extends BaseEntity {
    @Id
    @Column(name = "permission_name", length = 100)
    String permissionName;

    @Column(name = "description", length = 1000)
    String description;

    @OneToMany(mappedBy = "permission")
    @ToString.Exclude //// Bỏ qua để tránh vòng lặp
    List<RolePermission> listRolePermission;
}
