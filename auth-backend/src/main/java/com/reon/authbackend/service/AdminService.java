package com.reon.authbackend.service;

import com.reon.authbackend.dto.UserResponseDTO;
import com.reon.authbackend.model.Role;

import java.util.List;

public interface AdminService {
    List<UserResponseDTO> getUsers ();
    UserResponseDTO getUserById(String id);
    UserResponseDTO getUserByEmail(String email);
    UserResponseDTO getUserByUsername(String username);
    UserResponseDTO getUserByPhoneNumber(String phoneNumber);
    UserResponseDTO updateUserRole(String userId, Role role);
}
