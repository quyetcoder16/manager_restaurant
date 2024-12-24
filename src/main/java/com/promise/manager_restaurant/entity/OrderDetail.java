package com.promise.manager_restaurant.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "order_detail")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderDetail extends BaseEntity {
    @Id
    @Column(name = "order_id")
    String orderId;

    @Id
    @Column(name = "food_id")
    String foodId;

    @ManyToOne
    @JoinColumn(name = "order_id")
    Orders order;

    @ManyToOne
    @JoinColumn(name = "food_id")
    Food food;

    @Column(name = "quantity")
    int quantity;

}
