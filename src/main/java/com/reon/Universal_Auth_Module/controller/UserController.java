package com.reon.Universal_Auth_Module.controller;

import com.reon.Universal_Auth_Module.model.dto.LoginRequest;
import com.reon.Universal_Auth_Module.model.dto.UserDTO;
import com.reon.Universal_Auth_Module.model.dto.UserRegisterDTO;
import com.reon.Universal_Auth_Module.service.impl.UserServiceImpl;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173/")
public class UserController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final UserServiceImpl userService;
    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserDTO> registerUser(@Valid @RequestBody UserRegisterDTO registerDTO){
        logger.info("Received registration request for email: {}", registerDTO.getEmail());
        UserDTO registeredUser = userService.registerUser(registerDTO);
        return ResponseEntity.ok(registeredUser);
    }

    @PostMapping("/login")
    public ResponseEntity<UserDTO> loginUser(@Valid @RequestBody LoginRequest loginRequest){
        logger.info("Received login request for email: {}", loginRequest.getEmail());
        UserDTO user = userService.loginUser(loginRequest);
        return ResponseEntity.ok(user);
    }
}