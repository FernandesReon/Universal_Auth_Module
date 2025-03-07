package com.reon.Universal_Auth_Module.model.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRegisterDTO {
    private String name;
    private String username;
    private String email;
    private String password;
    private String gender;
    private String age;
    private String country;
    private String phone;
}
