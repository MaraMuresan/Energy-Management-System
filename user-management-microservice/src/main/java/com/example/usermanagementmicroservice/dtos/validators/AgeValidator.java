package com.example.usermanagementmicroservice.dtos.validators;

import org.springframework.stereotype.Component;
import com.example.usermanagementmicroservice.dtos.validators.annotation.AgeLimit;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

@Component
public class AgeValidator implements ConstraintValidator<AgeLimit, Integer> {

    private int ageLimit;

    @Override
    public void initialize(AgeLimit constraintAnnotation) {
        this.ageLimit = constraintAnnotation.limit();
    }

    @Override
    public boolean isValid(Integer inputAge, ConstraintValidatorContext constraintValidatorContext) {
        if (inputAge == null) return true;
        return inputAge >= ageLimit;
    }
}