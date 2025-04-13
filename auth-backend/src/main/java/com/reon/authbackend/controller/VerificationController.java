package com.reon.authbackend.controller;

import com.reon.authbackend.service.impl.EmailVerificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@CrossOrigin("*")
public class VerificationController {
    private final Logger logger = LoggerFactory.getLogger(VerificationController.class);
    private final EmailVerificationService emailVerificationService;

    public VerificationController(EmailVerificationService emailVerificationService) {
        this.emailVerificationService = emailVerificationService;
    }

    @GetMapping("/verify")
    public ResponseEntity<String> verifyEmail(@RequestParam("token") String token){
        logger.info("(Controller) :: Verification of email.");
        emailVerificationService.verifyToken(token);
        return ResponseEntity.ok("Email verified successfully. You can now log in.");
    }
}
