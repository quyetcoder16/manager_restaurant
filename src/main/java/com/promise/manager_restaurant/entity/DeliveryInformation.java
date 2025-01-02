package com.promise.manager_restaurant.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DeliveryInformation extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String deliId;

    String fullName;

    String phone;

    String address;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;

    @OneToMany(mappedBy = "deliveryInformation")
    List<Orders> orders;
}
