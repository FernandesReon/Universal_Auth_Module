package com.reon.authbackend.configuration;

import com.reon.authbackend.exception.UserNotFoundException;
import com.reon.authbackend.model.Provider;
import com.reon.authbackend.model.Role;
import com.reon.authbackend.model.User;
import com.reon.authbackend.repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.EnumSet;
import java.util.List;
import java.util.UUID;

@Component
public class OAuthSuccessHandler implements AuthenticationSuccessHandler {
    private final Logger logger = LoggerFactory.getLogger(OAuthSuccessHandler.class);
    private final UserRepository userRepository;

    public OAuthSuccessHandler(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        logger.info("In OauthSuccessHandler..");

        // identify the provider
        OAuth2AuthenticationToken authenticationToken = (OAuth2AuthenticationToken) authentication;
        String provider = authenticationToken.getAuthorizedClientRegistrationId();
        logger.info("Provider: {}", provider);

        // return the data from the provider
        DefaultOAuth2User userInfo = (DefaultOAuth2User) authentication.getPrincipal();
        userInfo.getAttributes().forEach((key, value) -> {
            logger.info("{} : {}", key, value);
        });

        String name = userInfo.getAttribute("name");
        String email = userInfo.getAttribute("email");
        String image = userInfo.getAttribute("picture");

        // Specifically for GitHub image only
        String avatar_url = userInfo.getAttribute("avatar_url");

        // Common for both Google and GitHub
        User user = new User();
        user.setId(UUID.randomUUID().toString());
        user.setPassword("");
        user.setGender(null);
        user.setCountry(null);
        user.setPhoneNumber(null);
        user.setAge(null);
        user.setRoles(EnumSet.of(Role.USER));
        user.setAddress("Hello " + userInfo.getAttribute("name").toString() + " you have loggedIn using "+ provider +" \nSet new address!");
        user.setAccountEnabled(true);
        user.setEmailVerified(true);
        user.setPhoneVerified(false);


        if (provider.equalsIgnoreCase("google")){
            user.setName(name);
            user.setUsername(email.substring(0, email.indexOf("@")));
            user.setEmail(email);
            user.setProvider(Provider.GOOGLE);
            user.setProviderId(userInfo.getName());
        }
        else if (provider.equalsIgnoreCase("github")){
            String check_email = userInfo.getAttribute("email") != null ?
                    email : userInfo.getAttribute("login") + "@gmail.com";
            user.setName(name);
            user.setUsername(userInfo.getAttribute("login"));
            user.setEmail(check_email.toLowerCase());
            user.setProvider(Provider.GITHUB);
            user.setProviderId(userInfo.getName());
        }
        else {
            logger.info("Unknown provider...");
        }

        User oauthUser = userRepository.findByEmail(user.getEmail()).orElseThrow(
                () -> new UserNotFoundException("User not found!")
        );
        if (oauthUser == null){
            userRepository.save(user);
            logger.info("New user saved with email: " + user.getEmail());
        }
        else {
            logger.info("User already exist with email: " + user.getEmail());
        }

        response.sendRedirect("/user/dashboard");
    }
}
