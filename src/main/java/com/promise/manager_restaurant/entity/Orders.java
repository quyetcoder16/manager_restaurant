package com.promise.manager_restaurant.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Entity
@Table(name = "orders")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Orders extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "order_id")
    String orderId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;

    @ManyToOne
    @JoinColumn(name = "res_id" , referencedColumnName = "res_id")
    Restaurant restaurant;

    @ManyToOne
    @JoinColumn(name = "order_status_id")
    OrderStatus orderStatus;

    @OneToMany(mappedBy = "order")
    List<OrderDetail> listOrderDetail;

    @ManyToOne
    @JoinColumn(name = "deli_id")
    DeliveryInformation deliveryInformation;

}
