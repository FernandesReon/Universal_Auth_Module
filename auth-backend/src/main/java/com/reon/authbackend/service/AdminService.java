package com.reon.authbackend.service;

import com.reon.authbackend.dto.UserRegisterDTO;
import com.reon.authbackend.dto.UserResponseDTO;
import com.reon.authbackend.model.Role;
import org.springframework.data.domain.Page;

import java.util.List;

public interface AdminService {
    List<UserResponseDTO> getUsers ();
    UserResponseDTO getUserById(String id);
    UserResponseDTO getUserByEmail(String email);
    UserResponseDTO getUserByUsername(String username);
    UserResponseDTO getUserByPhoneNumber(String phoneNumber);
    UserResponseDTO updateUserRole(String userId, Role role);
    List<UserResponseDTO> registerMultipleUsers(List<UserRegisterDTO> registerDTOS);

    // Pagination methods
    Page<UserResponseDTO> getPaginatedUsers(int pageNo, int pageSize);
}
