package com.example.demo.service.impl;

import com.example.demo.dto.user.UserRegistrationRequestDto;
import com.example.demo.dto.user.UserResponseDto;
import com.example.demo.exception.EntityNotFoundException;
import com.example.demo.exception.RegistrationException;
import com.example.demo.mapper.UserMapper;
import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.repository.role.RoleRepository;
import com.example.demo.repository.user.UserRepository;
import com.example.demo.service.UserService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private static final String ROLE_USER = "ROLE_USER";
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Override
    public UserResponseDto register(UserRegistrationRequestDto userRegistrationRequestDto) {
        if (userRepository.existsByEmail(userRegistrationRequestDto.getEmail())) {
            throw new RegistrationException(
                    "User already exists with email: " + userRegistrationRequestDto.getEmail()
            );
        }
        User user = userMapper.toModel(userRegistrationRequestDto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        List<Role> roles = roleRepository.findAll();
        Role role = roles.stream().filter(i -> i.getName().name().equals(ROLE_USER))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Role is not found: " + ROLE_USER));
        user.addRole(role);
        return userMapper.toDto(userRepository.save(user));
    }
}
