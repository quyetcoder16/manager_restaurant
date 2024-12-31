package com.promise.manager_restaurant.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class BooleanValidator implements ConstraintValidator<BooleanContraint, String> {

    @Override
    public void initialize(BooleanContraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }
        return value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false");
    }
}
