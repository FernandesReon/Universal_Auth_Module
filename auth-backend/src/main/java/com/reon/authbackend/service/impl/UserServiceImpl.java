package com.reon.authbackend.service.impl;

import com.reon.authbackend.dto.LoginDTO;
import com.reon.authbackend.dto.UserRegisterDTO;
import com.reon.authbackend.dto.UserResponseDTO;
import com.reon.authbackend.exception.EmailAlreadyExistsException;
import com.reon.authbackend.exception.InvalidCredentialsException;
import com.reon.authbackend.exception.UserNotFoundException;
import com.reon.authbackend.exception.UsernameAlreadyExistsException;
import com.reon.authbackend.mapper.UserMapper;
import com.reon.authbackend.model.Role;
import com.reon.authbackend.model.User;
import com.reon.authbackend.repository.UserRepository;
import com.reon.authbackend.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Caching;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.EnumSet;
import java.util.UUID;

import static com.reon.authbackend.mapper.UserMapper.mapToUser;
import static com.reon.authbackend.mapper.UserMapper.responseToUser;

@Service
public class UserServiceImpl implements UserService {
    private final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserRepository userRepository;
    private final EmailVerificationService emailVerificationService;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, EmailVerificationService emailVerificationService,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.emailVerificationService = emailVerificationService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @CacheEvict(value = "userCache", key = "'userInfo'")
    public UserResponseDTO createUser(UserRegisterDTO registerUser) {
        if (userRepository.existsByEmail(registerUser.getEmail())){
            throw new EmailAlreadyExistsException("A user with this email already exists: " + registerUser.getEmail());
        }

        if (userRepository.existsByUsername(registerUser.getUsername())){
            throw new UsernameAlreadyExistsException("A user with this username already exists: " + registerUser.getUsername());
        }

        logger.info("Creating a new user with email: {}", registerUser.getEmail());
        User user = mapToUser(registerUser);

        // Set a random id.
        String id= UUID.randomUUID().toString();
        user.setId(id);

        // Encrypt the password
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Set default role to user
        user.setRoles(EnumSet.of(Role.USER));

        // Set emailVerified and accountEnabled to false at registration
//        user.setAccountEnabled(false);
//        user.setEmailVerified(false);
        User saveUser = userRepository.save(user);

        // Create and send verification token
//        emailVerificationService.createAndSendVerificationToken(user);

        return UserMapper.responseToUser(saveUser);
    }

    @Override
    @Caching(
            put = @CachePut(value = "users", key = "#id"),
            evict = @CacheEvict(value = "userCache", key = "'userInfo'")
    )
    public UserResponseDTO updateUser(String id, UserRegisterDTO updateDTO) {
        logger.info("Updating an user with id: " + id);
        User exisitingUser = userRepository.findById(id).orElseThrow(
                () -> new UserNotFoundException("User not found with id: " + id)
        );
        UserMapper.applyUpdates(exisitingUser, updateDTO);
        User savedUser = userRepository.save(exisitingUser);
        logger.info("User updated successfully [id]: " + id);
        return UserMapper.responseToUser(savedUser);
    }


    @Override
    @Caching(
            evict = {
                    @CacheEvict(value = "users", key = "#id"),
                    @CacheEvict(value = "userCache", key = "'userInfo'")
            }
    )
    public void deleteUser(String id) {
        logger.info("Deleting user with id: " + id);
        userRepository.deleteById(id);
    }

    @Override
    public UserResponseDTO accessUser(LoginDTO loginDTO) {
        logger.info("Fetching user data with email : " + loginDTO.getEmail() + " for login request.");
        User searchUser = userRepository.findByEmail(loginDTO.getEmail()).orElseThrow(
                () -> new UserNotFoundException("User with email: " + loginDTO.getEmail() + " not found!")
        );

        if (!passwordEncoder.matches(loginDTO.getPassword(), searchUser.getPassword())){
            throw new InvalidCredentialsException("Invalid credentials");
        }
        return responseToUser(searchUser);
    }
}
