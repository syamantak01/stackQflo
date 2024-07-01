package com.dexcode.stackQflo.validations.validators;

import com.dexcode.stackQflo.repositories.UserRepository;
import com.dexcode.stackQflo.validations.annotations.UsernameUnique;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

public class UsernameUniqueValidator implements ConstraintValidator<UsernameUnique, String> {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void initialize(UsernameUnique constraintAnnotation) {
    }

    @Override
    public boolean isValid(String username, ConstraintValidatorContext context) {
        if(username == null || username.isBlank()){
            // handled by @NotBlank validator
            return true;
        }
        boolean exists = userRepository.existsUserByUsername(username);
        if (exists) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                    "Username '" + username + "' is already taken"
            ).addConstraintViolation();
        }

        return !exists;
    }
}
