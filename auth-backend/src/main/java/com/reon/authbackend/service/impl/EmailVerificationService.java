package com.reon.authbackend.service.impl;

import com.reon.authbackend.exception.InvalidTokenException;
import com.reon.authbackend.model.EmailVerificationToken;
import com.reon.authbackend.model.User;
import com.reon.authbackend.repository.EmailRepository;
import com.reon.authbackend.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class EmailVerificationService {
    private final Logger logger = LoggerFactory.getLogger(EmailVerificationService.class);
    // The generated token will expire after 1 hour of being generated.
    private static final int EXPIRATION_HOURS = 1;
    private final EmailRepository emailRepository;
    private final UserRepository userRepository;
    private final EmailServiceImpl emailService;

    public EmailVerificationService(EmailRepository emailRepository, UserRepository userRepository, EmailServiceImpl emailService) {
        this.emailRepository = emailRepository;
        this.userRepository = userRepository;
        this.emailService = emailService;
    }

    public void createAndSendVerificationToken(User user){
        String token = UUID.randomUUID().toString();
        LocalDateTime expiryDate = LocalDateTime.now().plusHours(EXPIRATION_HOURS);
        EmailVerificationToken verificationToken = new EmailVerificationToken(token, user, expiryDate);
        emailRepository.save(verificationToken);
        emailService.sendVerificationEmail(user, token);
    }

    public void verifyToken(String token) {
        Optional<EmailVerificationToken> optionalToken = emailRepository.findByToken(token);
        if (optionalToken.isEmpty()) {
            throw new InvalidTokenException("Invalid verification token");
        }

        EmailVerificationToken verificationToken = optionalToken.get();

        if (verificationToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new InvalidTokenException("Verification token has expired");
        }

        User user = verificationToken.getUser();
        user.setAccountEnabled(true);
        user.setEmailVerified(true);
        userRepository.save(user);

        // Once email is verified, delete the token.
        emailRepository.delete(verificationToken);
    }
}
