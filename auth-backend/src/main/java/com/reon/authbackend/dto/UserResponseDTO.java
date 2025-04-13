package com.reon.authbackend.dto;

import com.reon.authbackend.model.Provider;
import com.reon.authbackend.model.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDTO implements Serializable {
    private String id;
    private String name;
    private String username;
    private String email;
    private String gender;
    private String age;
    private String phoneNumber;
    private String country;
    private String address;
    private Set<Role> roles;
    private boolean emailVerified;
    private boolean phoneVerified;
    private Provider provider;
}
