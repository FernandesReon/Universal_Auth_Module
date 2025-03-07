package com.reon.Universal_Auth_Module.model.dto;

import com.reon.Universal_Auth_Module.model.Provider;
import com.reon.Universal_Auth_Module.model.Role;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {
    private String id;
    private String name;
    private String username;
    private String email;
    private String gender;
    private String age;
    private String country;
    private String phone;
    private String profilePic;
    private Role role;
    private boolean emailVerified;
    private boolean phoneVerified;
    private Provider provider;
}
