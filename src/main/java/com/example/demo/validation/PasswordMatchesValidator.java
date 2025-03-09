package com.example.demo.validation;

import com.example.demo.dto.user.UserRegistrationRequestDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Objects;

public class PasswordMatchesValidator
        implements ConstraintValidator<PasswordMatches, UserRegistrationRequestDto> {
    @Override
    public boolean isValid(UserRegistrationRequestDto user,
                           ConstraintValidatorContext context) {
        return Objects.equals(user.getPassword(), user.getRepeatPassword());
    }
}
