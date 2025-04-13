package com.reon.authbackend.service.impl;

import com.reon.authbackend.model.User;
import com.reon.authbackend.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomSecurityConfig implements UserDetailsService {
    private final UserRepository userRepository;

    public CustomSecurityConfig(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email).orElseThrow(
                () -> new UsernameNotFoundException("User with email: " + email + " not found.")
        );
    }
}
