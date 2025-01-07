package com.promise.manager_restaurant.dto.request.promo;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PromoUpdateRequest {

    @NotBlank
    String promoId;

    @NotBlank
    String resId;

    @NotBlank
    List<String> listFoods;

    @NotBlank
    Integer percent;

    @NotBlank
    LocalDate startDate;

    @NotBlank
    LocalDate endDate;

}
