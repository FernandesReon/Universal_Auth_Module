package com.reon.authbackend.service.impl;

import com.reon.authbackend.dto.UserResponseDTO;
import com.reon.authbackend.exception.UserNotFoundException;
import com.reon.authbackend.mapper.UserMapper;
import com.reon.authbackend.model.Role;
import com.reon.authbackend.model.User;
import com.reon.authbackend.repository.UserRepository;
import com.reon.authbackend.service.AdminService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdminServiceImpl implements AdminService {
    private static final Logger logger = LoggerFactory.getLogger(AdminServiceImpl.class);
    private final UserRepository userRepository;

    public AdminServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<UserResponseDTO> getUsers() {
        List<User> users = userRepository.findAll();
        return users.stream().map(UserMapper :: responseToUser).toList();
    }

    @Override
    @Cacheable(value = "users", key = "#id")
    public UserResponseDTO getUserById(String id) {
        logger.info("Fetching user with id: " + id);
        User optionalUser = userRepository.findById(id).orElseThrow(
                () -> new UserNotFoundException("User with id: " + id + " not found!")
        );
        return UserMapper.responseToUser(optionalUser);
    }

    @Override
    @Cacheable(value = "users", key = "#email")
    public UserResponseDTO getUserByEmail(String email) {
        logger.info("Fetching user with email: " + email);
        User optionalEmail = userRepository.findByEmail(email).orElseThrow(
                () -> new UserNotFoundException("User with email: " + email + " not found!")
        );
        return UserMapper.responseToUser(optionalEmail);
    }

    @Override
    @Cacheable(value = "users", key = "#username")
    public UserResponseDTO getUserByUsername(String username) {
        logger.info("Fetching user with username: " + username);
        User optionalUsername = userRepository.findByUsername(username).orElseThrow(
                () -> new UserNotFoundException("User with username: " + username + " not found!")
        );
        return UserMapper.responseToUser(optionalUsername);
    }

    @Override
    @Cacheable(value = "users", key = "#phoneNumber")
    public UserResponseDTO getUserByPhoneNumber(String phoneNumber) {
        logger.info("Fetching user with phoneNumber: " + phoneNumber);
        User optionalPhone = userRepository.findByPhoneNumber(phoneNumber).orElseThrow(
                ()-> new UserNotFoundException("User with phoneNumber: " + phoneNumber + "not found!")
        );
        return UserMapper.responseToUser(optionalPhone);
    }

    @Override
    @CachePut(value = "users", key = "#userId")
    public UserResponseDTO updateUserRole(String userId, Role role) {
        logger.info("Accessing Administrative Service for promoting user with id: " + userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));
        user.getRoles().add(role);
        User savedUser = userRepository.save(user);
        return UserMapper.responseToUser(savedUser);
    }
}