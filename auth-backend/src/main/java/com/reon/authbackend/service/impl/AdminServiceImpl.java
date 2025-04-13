package com.reon.authbackend.service.impl;

import com.reon.authbackend.dto.UserRegisterDTO;
import com.reon.authbackend.dto.UserResponseDTO;
import com.reon.authbackend.exception.EmailAlreadyExistsException;
import com.reon.authbackend.exception.UserNotFoundException;
import com.reon.authbackend.exception.UsernameAlreadyExistsException;
import com.reon.authbackend.mapper.UserMapper;
import com.reon.authbackend.model.Role;
import com.reon.authbackend.model.User;
import com.reon.authbackend.repository.UserRepository;
import com.reon.authbackend.service.AdminService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AdminServiceImpl implements AdminService {
    private static final Logger logger = LoggerFactory.getLogger(AdminServiceImpl.class);
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Cacheable(value = "userCache", key = "'userInfo'")
    public List<UserResponseDTO> getUsers() {
        List<User> users = userRepository.findAll();
        return users.stream().map(UserMapper :: responseToUser).toList();
    }

    @Override
    public Page<UserResponseDTO> getPaginatedUsers(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo -1, pageSize);
        Page<User> userPage = userRepository.findAll(pageable);
        return userPage.map(UserMapper :: responseToUser);
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

    @Override
    @CacheEvict(value = "userCache", key = "'userInfo'")
    public List<UserResponseDTO> registerMultipleUsers(List<UserRegisterDTO> registerDTOS) {
        logger.info("Attempting to register {} users", registerDTOS.size());

        if (registerDTOS == null || registerDTOS.isEmpty()){
            throw new IllegalArgumentException("User list cannot be null or empty");
        }

        Set<String> emails = new HashSet<>();
        Set<String> usernames = new HashSet<>();
        for (UserRegisterDTO dto : registerDTOS) {
            if (!emails.add(dto.getEmail())) {
                throw new EmailAlreadyExistsException("Duplicate email in input: " + dto.getEmail());
            }
            if (!usernames.add(dto.getUsername())) {
                throw new UsernameAlreadyExistsException("Duplicate username in input: " + dto.getUsername());
            }
        }

        // Check for existing emails and usernames in the database
        for (UserRegisterDTO dto : registerDTOS) {
            if (userRepository.existsByEmail(dto.getEmail())) {
                throw new EmailAlreadyExistsException("A user with this email already exists: " + dto.getEmail());
            }
            if (userRepository.existsByUsername(dto.getUsername())) {
                throw new UsernameAlreadyExistsException("A user with this username already exists: " + dto.getUsername());
            }
        }

        // Process and save users
        List<UserResponseDTO> savedUsers = new ArrayList<>();
        for (UserRegisterDTO dto : registerDTOS) {
            logger.info("Creating user with email: {}", dto.getEmail());
            User user = UserMapper.mapToUser(dto);

            // Generate a unique ID
            String id = UUID.randomUUID().toString();
            user.setId(id);

            // Encode the password
            user.setPassword(passwordEncoder.encode(user.getPassword()));

            // Set default role and timestamps
            user.setRoles(EnumSet.of(Role.USER));
            user.setAccountEnabled(true); // As per existing code, set to true for testing
            user.setEmailVerified(true);  // As per existing code

            // Save user
            User savedUser = userRepository.save(user);
            logger.info("User saved with id: {}", id);

            // Map to response DTO
            savedUsers.add(UserMapper.responseToUser(savedUser));
        }

        logger.info("Successfully registered {} users", savedUsers.size());
        return savedUsers;
    }
}