package com.promise.manager_restaurant.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Entity
@Table(name = "category")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Category extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "cate_id")
    String cateID;

    @Column(name = "name_category")
    String nameCategory;

    @OneToMany(mappedBy = "category")
    List<FoodCategory> listFoodCategory;

    @OneToMany(mappedBy = "category")
    List<RestaurantCategory> listRestaurantCategory;
}
