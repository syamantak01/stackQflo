package com.dexcode.stackQflo.validations.validators;

import com.dexcode.stackQflo.dto.PostDTO;
import com.dexcode.stackQflo.entities.PostType;
import com.dexcode.stackQflo.entities.User;
import com.dexcode.stackQflo.repositories.PostRepository;
import com.dexcode.stackQflo.repositories.PostTypeRepository;
import com.dexcode.stackQflo.repositories.TagRepository;
import com.dexcode.stackQflo.repositories.UserRepository;
import com.dexcode.stackQflo.validations.annotations.IsValidPostToCreate;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;

public class IsValidPostToCreateValidator implements ConstraintValidator<IsValidPostToCreate, PostDTO> {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostTypeRepository postTypeRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private TagRepository tagRepository;

    @Override
    public void initialize(IsValidPostToCreate constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(PostDTO postDTO, ConstraintValidatorContext constraintValidatorContext) {
        if(postDTO == null){
            return true;    // skip validation if postDTO is null
        }

        // User ID of user who created the post
        Long userId = postDTO.getUserId();

        // Type ID of post
        Long postTypeId = postDTO.getPostTypeId();

        // Tag IDs of post
        Set<Long> tagIds = postDTO.getTagIds();

        if(userId == null || postTypeId == null){
            return true;
        }


        User user = userRepository.findById(userId).orElse(null);
        PostType postType = postTypeRepository.findById(postTypeId).orElse(null);


        // If User ID doesn't exist
        if(user == null){
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext
                    .buildConstraintViolationWithTemplate("Invalid User ID: "+ userId)
                    .addPropertyNode("userId")
                    .addConstraintViolation();

            return false;
        }

        // If post_type id doesn't exist
        if(postType == null){
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext
                    .buildConstraintViolationWithTemplate("Invalid Post Type ID: "+ postTypeId)
                    .addPropertyNode("postTypeId")
                    .addConstraintViolation();

            return false;
        }

        // If post is of type "answer"
        if(postType.getTypeName().equalsIgnoreCase("answer")){
            Long parentQuestionId = postDTO.getParentQuestionId();

            // if there is no parent Question post associated with the answer post
            if( parentQuestionId == null){
                constraintValidatorContext.disableDefaultConstraintViolation();
                constraintValidatorContext
                        .buildConstraintViolationWithTemplate("Parent Question ID should not be null for an answer post")
                        .addConstraintViolation();
                return false;
            }

            // Parent Question Post of the Answer Post and the Answer Post cannot be the same - only update
            if(parentQuestionId.equals(postDTO.getPostId())){
                constraintValidatorContext.disableDefaultConstraintViolation();
                constraintValidatorContext
                        .buildConstraintViolationWithTemplate("Parent Question ID and Answer Post ID cannot be same")
                        .addConstraintViolation();
                return false;
            }

            // if post associated with Parent Question ID does not exist
            if(!postRepository.existsById(parentQuestionId)){
                constraintValidatorContext.disableDefaultConstraintViolation();
                constraintValidatorContext
                        .buildConstraintViolationWithTemplate("Parent Question ID: "+ parentQuestionId + " post does not exist")
                        .addPropertyNode("parentQuestionId")
                        .addConstraintViolation();
                return false;
            }


            // if post associated with Parent Question ID exists but is not of question type
            if(!postRepository.findById(parentQuestionId)
                    .get().getPostType().getTypeName()
                    .equalsIgnoreCase("question")
            ){
                constraintValidatorContext.disableDefaultConstraintViolation();;
                constraintValidatorContext
                        .buildConstraintViolationWithTemplate("Post ID: " + parentQuestionId + " is not a question")
                        .addPropertyNode("parentQuestionId")
                        .addConstraintViolation();
                return false;
            }
        }

        // If post is of type question
        if(postType.getTypeName().equalsIgnoreCase("question")){
            Long acceptedAnswerId = postDTO.getAcceptedAnswerId();

            // Accepted Answer is not mandatory
            if(acceptedAnswerId != null){

                // Accepted Answer Post cannot be the same as Question Post - only update
                if(acceptedAnswerId.equals(postDTO.getPostId())){
                    constraintValidatorContext.disableDefaultConstraintViolation();
                    constraintValidatorContext
                            .buildConstraintViolationWithTemplate("Accepted Answer Post cannot be the same as Question Post")
                            .addConstraintViolation();
                    return false;
                }

                // Accepted Answer Post does not exist - only update
                if(!postRepository.existsById(acceptedAnswerId)){
                    constraintValidatorContext.disableDefaultConstraintViolation();
                    constraintValidatorContext
                            .buildConstraintViolationWithTemplate("Accepted Answer ID: " + acceptedAnswerId + " post does not exist")
                            .addPropertyNode("acceptedAnswerId")
                            .addConstraintViolation();
                    return false;
                }

                // if post associated with Accepted Answer ID exists but is not of answer type - only update
                if(!postRepository.findById(acceptedAnswerId)
                        .get().getPostType().getTypeName()
                        .equalsIgnoreCase("answer")
                ){
                    constraintValidatorContext.disableDefaultConstraintViolation();;
                    constraintValidatorContext
                            .buildConstraintViolationWithTemplate("Post ID: " + acceptedAnswerId + " is not an answer")
                            .addPropertyNode("acceptedAnswerId")
                            .addConstraintViolation();
                    return false;
                }
            }
        }

        if(tagIds != null){
            boolean invalidTagFound = tagIds.stream().anyMatch(tagID -> {
                boolean tagExists = tagRepository.existsById(tagID);
                // if tag of tagID does not exist
                if (!tagExists) {
                    constraintValidatorContext.disableDefaultConstraintViolation();
                    constraintValidatorContext
                            .buildConstraintViolationWithTemplate("Tag with ID: " + tagID + " does not exist")
                            .addPropertyNode("tagId")
                            .addConstraintViolation();
                    return true; // Indicates invalid tag is found
                }
                return false; // indicates all tags are correct
            });

            // returns false is invalidTagFound is true
            return !invalidTagFound;
        }

        return true;
    }
}
