package com.example.authenticationmicroservice.controllers;

import com.example.authenticationmicroservice.dtos.JwtResponseDTO;
import com.example.authenticationmicroservice.dtos.LoginRequestDTO;
import com.example.authenticationmicroservice.dtos.RegisterRequestDTO;
import com.example.authenticationmicroservice.services.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@CrossOrigin
@RequestMapping("/authentication")
@Tag(name = "Authentication", description = "Handles user login, registration, and JWT validation")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/register")
    @Operation(
            summary = "Register new user",
            description = "Creates a new user record in the user-credentials database and returns success message."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "User registered successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid registration data")
    })
    public ResponseEntity<String> register(
            @Valid @RequestBody
            @Parameter(description = "User registration data") RegisterRequestDTO request) {
        authenticationService.register(request);
        return new ResponseEntity<>("User registered successfully.", HttpStatus.CREATED);
    }

    @PostMapping("/login")
    @Operation(
            summary = "User login",
            description = "Authenticates a user using username and password and returns a JWT token."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Login successful"),
            @ApiResponse(responseCode = "401", description = "Invalid username or password")
    })
    public ResponseEntity<JwtResponseDTO> login(
            @Valid @RequestBody
            @Parameter(description = "Login credentials: username and password") LoginRequestDTO request) {
        JwtResponseDTO response = authenticationService.login(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @RequestMapping(
            value = "/validate",
            method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.OPTIONS, RequestMethod.HEAD}
    )
    @Operation(
            summary = "Validate JWT token",
            description = "Validates a JWT token sent in the Authorization header. Returns 204 if valid, 401 if invalid."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Token is valid"),
            @ApiResponse(responseCode = "401", description = "Missing or invalid token")
    })
    public ResponseEntity<Void> validate(
            @RequestHeader(value = "Authorization", required = false)
            @Parameter(description = "Authorization header: Bearer <token>") String authHeader,
            HttpServletRequest request
    ) {
        String path = request.getRequestURI();

        if (path.contains("swagger") || path.contains("api-docs")) {
            return ResponseEntity.noContent().build();
        }

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
