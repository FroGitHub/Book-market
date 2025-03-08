package com.example.demo.dto.user;

import com.example.demo.validation.PasswordMatches;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@PasswordMatches
public class UserRegistrationRequestDto {
    @NotNull
    @Email
    private String email;
    @NotNull
    @Size(min = 8, message = "Password should be longer than 8")
    private String password;
    @Size(min = 8, message = "Password should be longer than 8")
    private String repeatPassword;
    @Size(max = 25, message = "FirstName should be longer than 25")
    private String firstName;
    @Size(max = 25, message = "LastName should be longer than 25")
    private String lastName;
    private String shippingAddress;

}
