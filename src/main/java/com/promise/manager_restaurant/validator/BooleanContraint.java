package com.promise.manager_restaurant.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@java.lang.annotation.Target({ java.lang.annotation.ElementType.FIELD }) // Áp dụng cho các trường (field) của class.
@java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.RUNTIME) // Annotation tồn tại ở thời điểm runtime để sử dụng cho xác thực.
@jakarta.validation.Constraint(validatedBy = { BooleanValidator.class }) // Xác định validator được sử dụng là DobValidator.
// @inteface để thông báo day la mot anotation
public @interface BooleanContraint {
    String message() default "Giá trị không hợp lệ. Phải là 'true' hoặc 'false'.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

