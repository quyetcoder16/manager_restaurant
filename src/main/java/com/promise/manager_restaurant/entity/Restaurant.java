package com.promise.manager_restaurant.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "restaurant")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Restaurant extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "res_id")
    String resID;

    @Column(name = "title")
    String title;

    @Column(name = "sub_title")
    String subTitle;

    @Lob
    @Column(name = "description")
    String description;

    @Column(name = "logo")
    String logo;

    @Column(name = "address")
    String address;

    @Column(name = "is_freeship")
    Boolean isFreeship;

    @Column(name = "open_date")
    LocalDate openDate;

    @Column(name = "background")
    String background;

    @Column(name = "phone")
    String phone;

    @Column(name = "email")
    String email;

    @ManyToOne
    @JoinColumn(name = "manager_id")
    User manager;

    @OneToMany(mappedBy = "restaurant")
    List<Gallery> listGallery;

    @OneToMany(mappedBy = "restaurant")
    List<Food> listFood;

    @OneToMany(mappedBy = "restaurant")
    List<RatingRestaurant> listRatingRestaurant;

    @OneToMany(mappedBy = "restaurant")
    List<RestaurantCategory> listRestaurantCategory;

    @OneToMany(mappedBy = "restaurant")
    List<Promo> listPromo;

    @OneToMany(mappedBy = "restaurant")
    List<Orders> listOrder;

}
