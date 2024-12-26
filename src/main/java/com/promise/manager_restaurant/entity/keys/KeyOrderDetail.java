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
public class KeyOrderDetail implements Serializable {

    @Column(name = "order_id")
    String orderId;


    @Column(name = "food_id")
    String foodId;
}
