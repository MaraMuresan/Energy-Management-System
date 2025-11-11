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

    @RequestMapping(
            value = "/validate",
            method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.OPTIONS, RequestMethod.HEAD}
    )
    public ResponseEntity<Void> validate(
            @RequestHeader(value = "Authorization", required = false) String authHeader,
            HttpServletRequest request
    ) {
        System.out.println("---- VALIDATE CALLED ----");
        System.out.println("Method: " + request.getMethod());
        System.out.println("Authorization header: " + authHeader);

        if ("OPTIONS".equalsIgnoreCase(request.getMethod()) || "HEAD".equalsIgnoreCase(request.getMethod())) {
            return ResponseEntity.ok()
                    .header("Content-Length", "0")
                    .build();
        }

        if (authHeader == null || authHeader.isBlank()) {
            System.out.println("No Authorization header found!");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .header("Content-Length", "0")
                    .build();
        }

        String token = authHeader.replace("Bearer", "").trim();
        boolean valid = authenticationService.validate(token);

        if (valid) {
            System.out.println("Token valid, returning 204 No Content");
            return ResponseEntity.noContent()
                    .header("Content-Length", "0")
                    .build();
        } else {
            System.out.println("Token invalid, returning 401");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .header("Content-Length", "0")
                    .build();
        }
    }





}
