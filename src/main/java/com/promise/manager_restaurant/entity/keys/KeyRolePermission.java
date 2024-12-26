package com.promise.manager_restaurant.entity.keys;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Data
public class KeyRolePermission implements Serializable {

    @Column(name = "role_name")
    String roleName;


    @Column(name = "permission_name")
    String permissionName;
}
