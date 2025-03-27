package com.reon.authbackend.controller;

import com.reon.authbackend.dto.LoginDTO;
import com.reon.authbackend.dto.UserRegisterDTO;
import com.reon.authbackend.dto.UserResponseDTO;
import com.reon.authbackend.service.impl.UserServiceImpl;
import com.reon.authbackend.validator.CreateValidationGroup;
import com.reon.authbackend.validator.UpdateValidationGroup;
import jakarta.validation.Valid;
import jakarta.validation.groups.Default;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@CrossOrigin("*")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final UserServiceImpl userService;
    private final AuthenticationManager authenticationManager;

    public UserController(UserServiceImpl userService, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> registerUser (
            @Validated({CreateValidationGroup.class, Default.class}) @RequestBody UserRegisterDTO registerDTO){
        logger.info("Registration controller :: registration for user: " + registerDTO.getEmail() + " ongoing.");
        UserResponseDTO newUser =  userService.createUser(registerDTO);
        logger.info("A new user registered with emailId: " + registerDTO.getEmail());
        return ResponseEntity.ok().body(newUser);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(
            @PathVariable String id,
            @Validated(UpdateValidationGroup.class) @RequestBody UserRegisterDTO updateDTO) {
        logger.info("Update controller :: updating user with id: " + id);
        UserResponseDTO updatedUser = userService.updateUser(id, updateDTO);
        logger.info("User with id: " + id + " updated successfully.");
        return ResponseEntity.ok().body(updatedUser);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        logger.info("Delete controller :: deleting user with id: " + id);
        userService.deleteUser(id);
        logger.info("User with id: " + id + " deleted successfully.");
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponseDTO> loginUser (@RequestBody LoginDTO loginDTO){
        logger.info("Login Controller :: fetching info for user with email: " + loginDTO.getEmail());

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDTO.getEmail(),
                        loginDTO.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserResponseDTO loggedInUser = userService.accessUser(loginDTO);
        logger.info("User loggedIn");
        return ResponseEntity.ok().body(loggedInUser);
    }
}
