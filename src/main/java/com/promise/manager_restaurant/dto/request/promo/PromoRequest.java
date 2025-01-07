package com.promise.manager_restaurant.dto.request.promo;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PromoRequest {

    String resId;

    List<String> listFoods;

    int percent;

    LocalDate startDate;

    @NotBlank
    String promoCode;

    LocalDate endDate;

}
