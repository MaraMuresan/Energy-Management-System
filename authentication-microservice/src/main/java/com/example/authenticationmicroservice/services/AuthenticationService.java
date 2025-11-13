package com.example.authenticationmicroservice.services;

import com.example.authenticationmicroservice.controllers.handlers.exceptions.model.DuplicateResourceException;
import com.example.authenticationmicroservice.controllers.handlers.exceptions.model.ParameterValidationException;
import com.example.authenticationmicroservice.controllers.handlers.exceptions.model.ResourceNotFoundException;
import com.example.authenticationmicroservice.dtos.JwtResponseDTO;
import com.example.authenticationmicroservice.dtos.LoginRequestDTO;
import com.example.authenticationmicroservice.dtos.RegisterRequestDTO;
import com.example.authenticationmicroservice.dtos.builders.JwtResponseBuilder;
import com.example.authenticationmicroservice.entities.UserCredentials;
import com.example.authenticationmicroservice.repositories.UserCredentialsRepository;
import com.example.authenticationmicroservice.utils.JwtUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthenticationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationService.class);

    private final UserCredentialsRepository repository;
    private final JwtUtils jwtUtils;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    public AuthenticationService(UserCredentialsRepository repository, JwtUtils jwtUtils) {
        this.repository = repository;
        this.jwtUtils = jwtUtils;
    }


    public UUID register(RegisterRequestDTO req) {
        if (repository.existsByUsername(req.getUsername())) {
            LOGGER.error("Username {} already exists", req.getUsername());
            throw new DuplicateResourceException(
                    "A user with this username already exists: " + req.getUsername()
            );
        }

        String encryptedPassword = passwordEncoder.encode(req.getPassword());

        UserCredentials user = new UserCredentials();
        user.setUsername(req.getUsername());
        user.setPassword(encryptedPassword);
        user.setRole(req.getRole());

        repository.save(user);
        LOGGER.debug("User registered successfully with id: {}", user.getId());

        RestTemplate restTemplate = new RestTemplate();
        String url = "http://traefik/user/internal";

        Map<String, Object> userPayload = Map.of(
                "id", user.getId(),
                "username", req.getUsername(),
                "address", req.getAddress(),
                "age", req.getAge(),
                "password", encryptedPassword,
                "role", req.getRole()
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(userPayload, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(url, requestEntity, String.class);
            LOGGER.info("User created in User microservice: {}", response.getStatusCode());
        } catch (org.springframework.web.client.HttpClientErrorException e) {
            String body = e.getResponseBodyAsString();
            LOGGER.error("User microservice rejected user creation: {}", body);

            repository.delete(user);

            String forwardedMsg = body;
            try {
                com.fasterxml.jackson.databind.JsonNode node =
                        new com.fasterxml.jackson.databind.ObjectMapper().readTree(body);
                if (node.has("message")) {
                    forwardedMsg = node.get("message").asText();
                }
            } catch (Exception ignore) { }

            throw new ParameterValidationException(forwardedMsg);
        } catch (Exception e) {
            LOGGER.error("Unexpected error contacting User microservice: {}", e.getMessage());
            repository.delete(user);
            throw new ParameterValidationException("Unexpected error: " + e.getMessage());
        }

        return user.getId();
    }


    public JwtResponseDTO login(LoginRequestDTO req) {
        Optional<UserCredentials> userOptional = repository.findByUsername(req.getUsername());
        if (!userOptional.isPresent()) {
            LOGGER.error("User {} not found", req.getUsername());
            throw new ResourceNotFoundException("User not found: " + req.getUsername());
        }

        UserCredentials user = userOptional.get();

        if (!passwordEncoder.matches(req.getPassword(), user.getPassword())) {
            LOGGER.error("Invalid password for username {}", req.getUsername());
            throw new ParameterValidationException("Invalid password for user: " + req.getUsername());
        }

        try {
            String token = jwtUtils.generateToken(user.getId(), user.getUsername(), user.getRole().name());
            LOGGER.debug("JWT token generated successfully for username {}", req.getUsername());
            return JwtResponseBuilder.toJwtResponseDTO(token, user.getRole().name());
        } catch (Exception e) {
            LOGGER.error("Error generating JWT token for user {}: {}", req.getUsername(), e.getMessage());
            throw new ParameterValidationException("Error generating token for user: " + req.getUsername());
        }
    }

    public boolean validate(String token) {
        boolean valid = jwtUtils.validateToken(token);
        if (valid) {
            LOGGER.debug("JWT validation successful.");
        } else {
            LOGGER.warn("JWT validation failed or token expired.");
        }
        return valid;
    }
}
