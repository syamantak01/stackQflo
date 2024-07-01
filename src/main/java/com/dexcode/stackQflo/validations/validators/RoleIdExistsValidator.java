package com.dexcode.stackQflo.validations.validators;

import com.dexcode.stackQflo.repositories.RoleRepository;
import com.dexcode.stackQflo.validations.annotations.RoleIdExists;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

public class RoleIdExistsValidator implements ConstraintValidator<RoleIdExists, Long> {
    @Autowired
    private RoleRepository roleRepository;


    @Override
    public void initialize(RoleIdExists constraintAnnotation) {
    }

    @Override
    public boolean isValid(Long roleId, ConstraintValidatorContext context) {
        if (roleId == null) {
            // handled by @NotBlank validator
            return true;
        }
        boolean exists = roleRepository.existsById(roleId);
        if (!exists) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                    "Role ID: " + roleId + " does not exist"
            ).addConstraintViolation();
        }

        return exists;
    }
}
