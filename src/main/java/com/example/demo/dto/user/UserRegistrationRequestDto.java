package com.example.demo.dto.user;

import com.example.demo.validation.PasswordMatches;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@PasswordMatches
public class UserRegistrationRequestDto {
    @NotBlank
    @Email
    private String email;
    @NotBlank
    @Size(min = 8, message = "Password should be longer than 8")
    private String password;
    @NotBlank
    @Size(min = 8, message = "Password should be longer than 8")
    private String repeatPassword;
    @NotBlank
    @Size(max = 25, message = "FirstName should be longer than 25")
    private String firstName;
    @NotBlank
    @Size(max = 25, message = "LastName should be longer than 25")
    private String lastName;
    private String shippingAddress;

}
