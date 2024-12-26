package com.promise.manager_restaurant.entity;

import com.promise.manager_restaurant.entity.keys.KeyFoodCategory;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "food_category")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FoodCategory extends BaseEntity {

    @EmbeddedId
    KeyFoodCategory keyFoodCategory;

    @ManyToOne
    @JoinColumn(name = "food_id", insertable = false, updatable = false)
    Food food;

    @ManyToOne
    @JoinColumn(name = "category_id", insertable = false, updatable = false)
    Category category;
}
