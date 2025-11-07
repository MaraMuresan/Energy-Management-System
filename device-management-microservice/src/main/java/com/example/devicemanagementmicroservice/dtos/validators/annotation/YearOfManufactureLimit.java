package com.example.devicemanagementmicroservice.dtos.validators.annotation;

import com.example.devicemanagementmicroservice.dtos.validators.YearOfManufactureValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {YearOfManufactureValidator.class})
public @interface YearOfManufactureLimit {

    int limit() default 2000;

    String message() default "Year of manufacture does not match the required 2015 year limit";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
