package com.reon.Universal_Auth_Module.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Entity
@Table(name = "user_info")
public class User {
    @Id
    private String id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    private String gender;
    private String age;
    private String country;
    private String address;
    private String phone;
    private String profilePic;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private Role role = Role.USER;

    private boolean accountEnabled = true;
    private boolean phoneVerified = false;
    private boolean emailVerified = false;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private Provider provider = Provider.SELF;

    @Column(unique = true)
    private String providerId;
}
