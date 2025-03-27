package com.reon.authbackend.service.impl;

import com.reon.authbackend.model.User;
import com.reon.authbackend.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {
    private final Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);
    private final JavaMailSender mailSender;

    @Value("${app.verificationUrl}")
    private String verificationUrl;

    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendVerificationEmail(User user, String token) {
        logger.info("Sending verification email to user with email address: " + user.getEmail());
        String recipientAddress = user.getEmail();
        String subject = "Email Verification for Universal Auth Module.";
        String confirmationUrl = verificationUrl + "?token=" + token;
        String message =
                "Thank you for registering. Please click the link below to verify your email:\n" + confirmationUrl;

        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(recipientAddress);
        email.setSubject(subject);
        email.setText(message);
        mailSender.send(email);
    }
}
