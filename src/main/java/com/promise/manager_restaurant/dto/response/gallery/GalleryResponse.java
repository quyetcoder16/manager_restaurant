package com.promise.manager_restaurant.dto.response.gallery;

import lombok.*;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GalleryResponse {
    String image;
}
