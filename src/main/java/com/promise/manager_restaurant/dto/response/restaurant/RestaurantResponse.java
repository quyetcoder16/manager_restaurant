package com.promise.manager_restaurant.dto.response.restaurant;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.promise.manager_restaurant.dto.response.category.CategoryResponse;
import com.promise.manager_restaurant.dto.response.gallery.GalleryResponse;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RestaurantResponse {
    String title;

    String subTitle;

    String description;

    String logo;

    String address;

    Boolean isFreeship;

    LocalDate openDate;

    String background;

    String phone;

    String email;

}
