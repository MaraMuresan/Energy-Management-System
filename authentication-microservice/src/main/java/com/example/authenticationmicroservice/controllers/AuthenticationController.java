package com.example.authenticationmicroservice.controllers;

import com.example.authenticationmicroservice.dtos.JwtResponseDTO;
import com.example.authenticationmicroservice.dtos.LoginRequestDTO;
import com.example.authenticationmicroservice.dtos.RegisterRequestDTO;
import com.example.authenticationmicroservice.services.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/authentication")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterRequestDTO request) {
        authenticationService.register(request);
        return new ResponseEntity<>("User registered successfully.", HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponseDTO> login(@Valid @RequestBody LoginRequestDTO request) {
        JwtResponseDTO response = authenticationService.login(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/validate")
    public ResponseEntity<Void> validate(@RequestHeader Map<String, String> headers) {
        System.out.println("---- VALIDATE CALLED ----");
        headers.forEach((k, v) -> System.out.println(k + ": " + v));

        String header = headers.getOrDefault("authorization",
                headers.getOrDefault("Authorization",
                        headers.getOrDefault("bearer", headers.get("Bearer"))));

        if (header == null) {
            System.out.println("No Authorization header found!");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        if (!header.startsWith("Bearer ")) {
            header = "Bearer " + header;
        }

        String token = header.substring(7).trim();
        boolean valid = authenticationService.validate(token);

        if (valid) {
            System.out.println("Token valid, returning 204");
            return ResponseEntity.noContent().build();
        } else {
            System.out.println("Token invalid, returning 401");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

}
