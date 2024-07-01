package com.dexcode.stackQflo.validations.validators;

import com.dexcode.stackQflo.repositories.UserRepository;
import com.dexcode.stackQflo.validations.annotations.EmailUnique;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

public class EmailUniqueValidator implements ConstraintValidator<EmailUnique, String> {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void initialize(EmailUnique constraintAnnotation) {
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        if(email == null || email.isBlank()){
            // handled by @NotBlank validator
            return true;
        }

        boolean exists = userRepository.existsUserByEmail(email);

        if(exists){
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                    "Email '" + email + "' is already registered"
            ).addConstraintViolation();
        }
        return !exists;
    }
}
