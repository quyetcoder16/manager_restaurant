package com.promise.manager_restaurant.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "restaurant_category")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RestaurantCategory extends BaseEntity {
    @Id
    @Column(name = "res_id")
    String resId;

    @Id
    @Column(name = "cate_id")
    String cateId;

    @ManyToOne
    @JoinColumn(name = "res_id")
    Restaurant restaurant;

    @ManyToOne
    @JoinColumn(name = "cate_id")
    Category category;
}
