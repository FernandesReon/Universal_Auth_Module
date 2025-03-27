package com.reon.authbackend.service;

import com.reon.authbackend.model.User;

public interface EmailService {
    void sendVerificationEmail(User user, String token);
}
