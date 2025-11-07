package com.example.devicemanagementmicroservice.dtos.validators;

import com.example.devicemanagementmicroservice.dtos.validators.annotation.YearOfManufactureLimit;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

@Component
public class YearOfManufactureValidator implements ConstraintValidator<YearOfManufactureLimit, Integer> {

    private int yearOfManufactureLimit;

    @Override
    public void initialize(YearOfManufactureLimit constraintAnnotation) {
        this.yearOfManufactureLimit = constraintAnnotation.limit();
    }

    @Override
    public boolean isValid(Integer inputYearOfManufacture, ConstraintValidatorContext constraintValidatorContext) {
        return inputYearOfManufacture >= yearOfManufactureLimit;
    }
}