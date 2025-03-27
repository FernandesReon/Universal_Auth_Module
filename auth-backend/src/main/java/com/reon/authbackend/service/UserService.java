package com.reon.authbackend.service;

import com.reon.authbackend.dto.LoginDTO;
import com.reon.authbackend.dto.UserRegisterDTO;
import com.reon.authbackend.dto.UserResponseDTO;

public interface UserService {
    UserResponseDTO createUser (UserRegisterDTO registerUser);
    UserResponseDTO updateUser (String id, UserRegisterDTO updateDTO);
    void deleteUser (String id);

    // login
    UserResponseDTO accessUser (LoginDTO loginDTO);
}
