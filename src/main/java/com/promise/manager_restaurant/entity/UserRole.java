package com.promise.manager_restaurant.entity;

import com.promise.manager_restaurant.entity.keys.KeyUserRoleId;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "user_role")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserRole extends BaseEntity {

    @EmbeddedId
    KeyUserRoleId keyUserRoleId;

    @ManyToOne
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    User user;

    @ManyToOne
    @JoinColumn(name = "role_name", insertable = false, updatable = false)
    Role role;
}
