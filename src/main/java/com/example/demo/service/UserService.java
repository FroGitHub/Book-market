package com.example.demo.service;

import com.example.demo.dto.user.UserRegistrationRequestDto;
import com.example.demo.dto.user.UserResponseDto;

public interface UserService {

    public UserResponseDto register(UserRegistrationRequestDto userRegistrationRequestDto);
}
