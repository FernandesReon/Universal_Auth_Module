package com.reon.Universal_Auth_Module.service.impl;

import com.reon.Universal_Auth_Module.model.User;
import com.reon.Universal_Auth_Module.model.dto.UserDTO;
import com.reon.Universal_Auth_Module.model.dto.UserRegisterDTO;
import com.reon.Universal_Auth_Module.repository.UserRepository;
import com.reon.Universal_Auth_Module.service.UserService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public UserDTO registerUser(UserRegisterDTO userRegisterDTO) {
        logger.info("Incoming data from frontend (UserRegistration form)");
        User user = mapToUser(userRegisterDTO);
        logger.info("Save the user in the database");
        String userId = UUID.randomUUID().toString();
        user.setId(userId);
        User savedUser = userRepository.save(user);
        logger.info("Mapping the saved User entity to UserDTO");
        return mapToUserDTO(savedUser);
    }

    @Override
    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(this::mapToUserDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public UserDTO updateUser(UserRegisterDTO userUpdateDTO) {
        User existingUser = userRepository.findByEmail(userUpdateDTO.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found with email : " + userUpdateDTO.getEmail()));
        logger.info("Updating the user: " + userUpdateDTO.getEmail());
        existingUser.setName(userUpdateDTO.getName());
        existingUser.setUsername(userUpdateDTO.getUsername());
        existingUser.setGender(userUpdateDTO.getGender());
        existingUser.setAge(userUpdateDTO.getAge());
        existingUser.setCountry(userUpdateDTO.getCountry());
        existingUser.setPhone(userUpdateDTO.getPhone());

        if (userUpdateDTO.getPassword() != null && !userUpdateDTO.getPassword().isEmpty()){
            existingUser.setPassword(userUpdateDTO.getPassword());
        }

        User updatedUser = userRepository.save(existingUser);
        return mapToUserDTO(updatedUser);
    }

    @Override
    public void deleteUser(String id) {
        userRepository.deleteById(id);
    }

    @Override
    public UserDTO getUserByEmail(String email) {
        User user = userRepository.findByEmail(email).
                orElseThrow(() -> new RuntimeException("User with email : " + email + " not found!"));
        return mapToUserDTO(user);
    }

    // Convert UserRegisterDTO to User entity
    private User mapToUser(UserRegisterDTO registerInfo){
        User user = new User();
        user.setName(registerInfo.getName());
        user.setUsername(registerInfo.getUsername());
        user.setEmail(registerInfo.getEmail());
        user.setPassword(registerInfo.getPassword());
        user.setGender(registerInfo.getGender());
        user.setAge(registerInfo.getAge());
        user.setCountry(registerInfo.getCountry());
        user.setPhone(registerInfo.getPhone());
        return user;
    }

    // Convert User entity to UserDTO
    private UserDTO mapToUserDTO(User user){
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setName(user.getName());
        userDTO.setUsername(user.getUsername());
        userDTO.setEmail(user.getEmail());
        userDTO.setGender(user.getGender());
        userDTO.setAge(user.getAge());
        userDTO.setCountry(user.getCountry());
        userDTO.setPhone(user.getPhone());
        userDTO.setProfilePic(user.getProfilePic());
        userDTO.setRole(user.getRole());
        userDTO.setEmailVerified(user.isEmailVerified());
        userDTO.setPhoneVerified(user.isPhoneVerified());
        userDTO.setProvider(user.getProvider());
        return userDTO;
    }
}
