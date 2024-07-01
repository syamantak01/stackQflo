package com.dexcode.stackQflo.validations.annotations;

import com.dexcode.stackQflo.validations.validators.RoleIdExistsValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = RoleIdExistsValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RoleIdExists {
    String message() default "Role ID does not exist";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
