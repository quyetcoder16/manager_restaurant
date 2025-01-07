package com.promise.manager_restaurant.dto.response.promo;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PromoReponse {

    String promoId;

    String promoCode;

    String resId;

    List<String> foodIds;

    int percent;

    LocalDate startDate;

    LocalDate endDate;

}
