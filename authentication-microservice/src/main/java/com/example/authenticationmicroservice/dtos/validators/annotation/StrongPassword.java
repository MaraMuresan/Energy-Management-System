package com.example.authenticationmicroservice.dtos.validators.annotation;

import com.example.authenticationmicroservice.dtos.validators.StrongPasswordValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {StrongPasswordValidator.class})
public @interface StrongPassword {
    String message() default "Password must be at least 8 characters long, contain one uppercase letter, one lowercase letter, and one digit.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
