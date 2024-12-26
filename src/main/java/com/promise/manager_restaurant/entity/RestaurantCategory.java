package com.promise.manager_restaurant.entity;

import com.promise.manager_restaurant.entity.keys.KeyRestaurantCategory;
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

    @EmbeddedId
    KeyRestaurantCategory keyRestaurantCategory;

    @ManyToOne
    @JoinColumn(name = "res_id", insertable = false, updatable = false)
    Restaurant restaurant;

    @ManyToOne
    @JoinColumn(name = "cate_id", insertable = false, updatable = false)
    Category category;
}
