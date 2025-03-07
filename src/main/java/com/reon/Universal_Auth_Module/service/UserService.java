package com.reon.Universal_Auth_Module.service;

import com.reon.Universal_Auth_Module.model.User;
import com.reon.Universal_Auth_Module.model.dto.UserDTO;
import com.reon.Universal_Auth_Module.model.dto.UserRegisterDTO;

import java.util.List;

public interface UserService {
    UserDTO registerUser(UserRegisterDTO userRegisterDTO);
    List<UserDTO> getAllUsers();
    UserDTO updateUser(UserRegisterDTO userUpdateDTO);
    void deleteUser(String id);
    UserDTO getUserByEmail(String email);
}
