package com.reon.Universal_Auth_Module.service.impl;

import com.reon.Universal_Auth_Module.model.User;
import com.reon.Universal_Auth_Module.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class CustomConfig implements UserDetailsService {
    private final UserRepository userRepository;

    public CustomConfig(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User with email: " + email + " not found"));
        return user;
    }
}
