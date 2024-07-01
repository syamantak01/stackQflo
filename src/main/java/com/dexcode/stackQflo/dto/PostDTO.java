package com.dexcode.stackQflo.dto;

import com.dexcode.stackQflo.validations.ValidationGroups;
import com.dexcode.stackQflo.validations.annotations.IsValidPost;
import com.dexcode.stackQflo.validations.annotations.IsValidPostToUpdate;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@IsValidPost(groups = {ValidationGroups.Create.class, ValidationGroups.Update.class})
@IsValidPostToUpdate(groups = ValidationGroups.Update.class)
public class PostDTO {

    private Long postId;

    @NotNull(message = "User ID is required", groups = ValidationGroups.Create.class)
    private Long userId;

    @NotBlank(message = "Title cannot be null or empty", groups = ValidationGroups.Create.class)
    @Size(max=250, message="Title cannot be longer than 250 characters", groups = {ValidationGroups.Create.class, ValidationGroups.Update.class})
    private String title;

    @NotBlank(message = "Description cannot be null or empty", groups = ValidationGroups.Create.class)
    private String description;

    private String image;

    @PastOrPresent(message = "Timestamp must be in past or present", groups = {ValidationGroups.Create.class, ValidationGroups.Update.class})
    private LocalDateTime timestamp;

    private Long parentQuestionId;

    @Null(message = "Accepted Answer is not decided while creating Post", groups = ValidationGroups.Create.class)
    private Long acceptedAnswerId;

    @NotNull(message = "Post Type is required", groups = ValidationGroups.Create.class)
    private Long postTypeId;

    private Set<Long> tagIds;

}
