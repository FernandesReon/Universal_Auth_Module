package com.reon.authbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class AuthBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthBackendApplication.class, args);
    }

}
