package com.example.demo.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.lang.reflect.Field;
import java.util.Objects;

public class FieldMatchesValidator implements ConstraintValidator<FieldMatches, Object> {
    private String[] fieldNames;

    @Override
    public void initialize(FieldMatches constraintAnnotation) {
        this.fieldNames = constraintAnnotation.fields();
    }

    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext context) {
        try {
            for (int i = 0; i < fieldNames.length - 1; i++) {
                Field firstField = obj.getClass().getDeclaredField(fieldNames[i]);
                Field secondField = obj.getClass().getDeclaredField(fieldNames[i + 1]);

                firstField.setAccessible(true);
                secondField.setAccessible(true);

                if (!Objects.equals(firstField.get(obj), secondField.get(obj))) {
                    return false;
                }
            }
            return true;
        } catch (NoSuchFieldException | IllegalAccessException e) {
            return false;
        }
    }
}
