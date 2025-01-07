package com.promise.manager_restaurant.repository;

import com.promise.manager_restaurant.entity.Promo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PromoRepository extends JpaRepository<Promo, String> {
    boolean existsPromoByPromoCode(String promoCode);

    Promo findByPromoCode(String promoCode);
}
