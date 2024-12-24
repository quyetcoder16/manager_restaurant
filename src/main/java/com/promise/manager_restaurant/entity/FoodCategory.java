package com.promise.manager_restaurant.entity;

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
    @Id
    @Column(name = "food_id")
    String foodId;

    @Id
    @Column(name = "category_id")
    String categoryId;

    @ManyToOne
    @JoinColumn(name = "food_id")
    Food food;

    @ManyToOne
    @JoinColumn(name = "category_id")
    Category category;
}
