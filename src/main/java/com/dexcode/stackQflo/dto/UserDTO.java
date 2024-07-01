package com.dexcode.stackQflo.dto;

import com.dexcode.stackQflo.validations.ValidationGroups;
import com.dexcode.stackQflo.validations.annotations.EmailUnique;
import com.dexcode.stackQflo.validations.annotations.RoleIdExists;
import com.dexcode.stackQflo.validations.annotations.UsernameUnique;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO {

    private Long userId;

    @NotBlank(message = "User name cannot be null or empty", groups = ValidationGroups.Create.class)
    @Size(min = 3, max = 50, message = "User name must be between 3 and 50 characters", groups = {ValidationGroups.Create.class, ValidationGroups.Update.class})
    @UsernameUnique(groups = {ValidationGroups.Create.class, ValidationGroups.Update.class})
    private String username;

    @NotBlank(message = "Email cannot be null or empty", groups = ValidationGroups.Create.class)
    @Email(message = "Email should be valid", groups = {ValidationGroups.Create.class, ValidationGroups.Update.class})
    @EmailUnique(groups = {ValidationGroups.Create.class, ValidationGroups.Update.class})
    private String email;

    @NotBlank(message = "Password cannot be null or empty", groups = ValidationGroups.Create.class)
    @Size(min = 8, message = "Password must be at least 8 characters", groups = {ValidationGroups.Create.class, ValidationGroups.Update.class})
    private String password;

    private String about;

    @NotNull(message = "Role ID cannot be null", groups = ValidationGroups.Create.class)
    @RoleIdExists(groups = {ValidationGroups.Create.class, ValidationGroups.Update.class})
    private Long roleId;
}
