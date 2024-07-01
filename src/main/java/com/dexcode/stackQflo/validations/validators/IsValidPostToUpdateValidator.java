package com.dexcode.stackQflo.validations.validators;

import com.dexcode.stackQflo.dto.PostDTO;
import com.dexcode.stackQflo.validations.annotations.IsValidPostToUpdate;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class IsValidPostToUpdateValidator implements ConstraintValidator<IsValidPostToUpdate, PostDTO> {

    @Override
    public void initialize(IsValidPostToUpdate constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(PostDTO postDTO, ConstraintValidatorContext constraintValidatorContext) {
        if(postDTO == null){
            return true;    // skip validation if postDTO is null
        }

        // Post ID of existing post cannot be updated
        if(postDTO.getPostId() != null){
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext
                    .buildConstraintViolationWithTemplate("Post ID of existing post cannot be updated")
                    .addPropertyNode("postId")
                    .addConstraintViolation();
            return false;
        }

        // User ID of existing post cannot be updated
        if(postDTO.getUserId() != null){
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext
                    .buildConstraintViolationWithTemplate("User who created existing post cannot be updated")
                    .addPropertyNode("userId")
                    .addConstraintViolation();
            return false;
        }

        // Post Type of existing post cannot be updated
        if(postDTO.getPostTypeId() != null){
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext
                    .buildConstraintViolationWithTemplate("PostType of existing post cannot be updated")
                    .addPropertyNode("postTypeId")
                    .addConstraintViolation();
            return false;
        }

        // Parent Question of existing post cannot be updated
        if(postDTO.getParentQuestionId() != null){
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext
                    .buildConstraintViolationWithTemplate("Parent Question cannot be updated")
                    .addPropertyNode("parentQuestionId")
                    .addConstraintViolation();
            return false;
        }

        return true;
    }
}
