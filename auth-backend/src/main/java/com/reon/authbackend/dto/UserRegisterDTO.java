package com.reon.authbackend.dto;

import com.reon.authbackend.validator.CreateValidationGroup;
import com.reon.authbackend.validator.UpdateValidationGroup;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserRegisterDTO {
    @NotBlank(
            groups = CreateValidationGroup.class,
            message = "Name is required")
    private String name;

    @NotBlank(
            groups = CreateValidationGroup.class,
            message = "Username is required")
    @Size(max = 10, message = "Username can be maximum of 10 characters")
    private String username;

    @NotBlank(
            groups = CreateValidationGroup.class,
            message = "Email is required")
    @Email(message = "Invalid format")
    private String email;

    @NotBlank(
            groups = CreateValidationGroup.class,
            message = "Password is required")
    @Size(min = 10, max = 64, message = "Password has to be minimum of 16 characters")
    private String password;

    private String gender;
    private String age;
    private String country;
    private String address;
    private String phoneNumber;
}
