package com.promise.manager_restaurant.dto.request.restaurant;

import com.promise.manager_restaurant.validator.BooleanContraint;
import jakarta.persistence.Column;
import jakarta.persistence.Lob;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RestaurantCreationRequest {

    String title;

    String subTitle;

    String description;

    String logo;

    String address;

    @BooleanContraint(message = "BOOLEAN_FAILED")
    String isFreeship;

    String openDate;

    MultipartFile background;

    String phone;

    String email;

}
