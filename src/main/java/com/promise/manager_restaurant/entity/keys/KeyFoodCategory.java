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
public class KeyFoodCategory implements Serializable {
    @Column(name = "food_id")
    String foodId;

    @Column(name = "category_id")
    String categoryId;
}
