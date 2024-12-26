package com.promise.manager_restaurant.entity;

import com.promise.manager_restaurant.entity.keys.KeyOrderDetail;
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

    @EmbeddedId
    KeyOrderDetail keyOrderDetail;

    @ManyToOne
    @JoinColumn(name = "order_id", insertable = false, updatable = false)
    Orders order;

    @ManyToOne
    @JoinColumn(name = "food_id", insertable = false, updatable = false)
    Food food;

    @Column(name = "quantity")
    int quantity;

}
