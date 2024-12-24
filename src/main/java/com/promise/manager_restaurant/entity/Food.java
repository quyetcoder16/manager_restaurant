package com.promise.manager_restaurant.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "food")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Food {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "food_id")
    String foodId;

    @Column(name = "title")
    String title;

    @Lob
    @Column(name = "description")
    String description;

    @Column(name = "image")
    String image;

    @Column(name = "time_ship")
    String timeShip;

    @Column(name = "price")
    double price;

    @ManyToOne
    @JoinColumn(name = "res_id")
    Restaurant restaurant;
}
