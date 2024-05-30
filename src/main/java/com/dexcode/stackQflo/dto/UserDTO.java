package com.dexcode.stackQflo.dto;

import com.dexcode.stackQflo.validations.EmailUnique;
import com.dexcode.stackQflo.validations.RoleIdExists;
import com.dexcode.stackQflo.validations.UsernameUnique;
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

    @NotBlank(message = "User name cannot be null or empty")
    @Size(min = 3, max = 50, message = "User name must be between 3 and 50 characters")
    @UsernameUnique
    private String username;

    @NotBlank(message = "Email cannot be null or empty")
    @Email(message = "Email should be valid")
    @EmailUnique
    private String email;

    @NotBlank(message = "Password cannot be null or empty")
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;

    private String about;

    @NotNull(message = "Role ID cannot be null")
    @RoleIdExists
    private Long roleId;
}
