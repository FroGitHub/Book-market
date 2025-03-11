package com.example.demo.controller;

import com.example.demo.dto.user.UserRegistrationRequestDto;
import com.example.demo.dto.user.UserResponseDto;
import com.example.demo.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@Tag(name = "Authentication", description = "Endpoints for user authentication")
public class AuthController {

    private final UserService userService;

    @Operation(summary = "Registration",
            description = "Takes user data, validates them and returns registered user")
    @GetMapping("/registration")
    public UserResponseDto register(@RequestBody @Valid UserRegistrationRequestDto request) {
        return userService.register(request);
    }
}
