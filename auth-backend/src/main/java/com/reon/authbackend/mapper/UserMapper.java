package com.reon.authbackend.mapper;

import com.reon.authbackend.dto.UserRegisterDTO;
import com.reon.authbackend.dto.UserResponseDTO;
import com.reon.authbackend.model.Role;
import com.reon.authbackend.model.User;

import java.util.EnumSet;

public class UserMapper {
    public static User mapToUser(UserRegisterDTO registerDTO) {
        User user = new User();
        user.setName(registerDTO.getName());
        user.setUsername(registerDTO.getUsername());
        user.setEmail(registerDTO.getEmail());
        user.setPassword(registerDTO.getPassword());
        user.setGender(registerDTO.getGender());
        user.setAge(registerDTO.getAge());
        user.setCountry(registerDTO.getCountry());
        user.setAddress(registerDTO.getAddress());
        user.setPhoneNumber(registerDTO.getPhoneNumber());
        return user;
    }

    public static UserResponseDTO responseToUser(User user) {
        UserResponseDTO response = new UserResponseDTO();
        response.setId(user.getId());
        response.setName(user.getName());
        response.setUsername(user.getEntityUsername());
        response.setEmail(user.getEmail());
        response.setGender(user.getGender());
        response.setAge(user.getAge());
        response.setCountry(user.getCountry());
        response.setAddress(user.getAddress());
        response.setPhoneNumber(user.getPhoneNumber());
        response.setRoles(user.getRoles());
        response.setEmailVerified(user.isEmailVerified());
        response.setPhoneVerified(user.isPhoneVerified());
        response.setProvider(user.getProvider());
        return response;
    }

    public static void applyUpdates(User user, UserRegisterDTO updateDTO){
        if (updateDTO.getName() != null && !updateDTO.getName().isBlank()){
            user.setName(updateDTO.getName());
        }
        if (updateDTO.getUsername() != null && !updateDTO.getUsername().isBlank()) {
            user.setUsername(updateDTO.getUsername());
        }
        if (updateDTO.getEmail() != null && !updateDTO.getEmail().isBlank()) {
            user.setEmail(updateDTO.getEmail());
        }
        if (updateDTO.getPassword() != null && !updateDTO.getPassword().isBlank()) {
            user.setPassword(updateDTO.getPassword());
        }
        if (updateDTO.getGender() != null && !updateDTO.getGender().isBlank()) {
            user.setGender(updateDTO.getGender());
        }
        if (updateDTO.getAge() != null && !updateDTO.getAge().isBlank()) {
            user.setAge(updateDTO.getAge());
        }
        if (updateDTO.getCountry() != null && !updateDTO.getCountry().isBlank()) {
            user.setCountry(updateDTO.getCountry());
        }
        if (updateDTO.getAddress() != null && !updateDTO.getAddress().isBlank()) {
            user.setAddress(updateDTO.getAddress());
        }
        if (updateDTO.getPhoneNumber() != null && !updateDTO.getPhoneNumber().isBlank()){
            user.setPhoneNumber(updateDTO.getPhoneNumber());
        }
    }
}
