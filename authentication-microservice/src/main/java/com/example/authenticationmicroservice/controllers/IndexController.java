package com.example.authenticationmicroservice.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.Parameter;

@RestController
@CrossOrigin
@Tag(name = "Authentication Status", description = "Health check for Authentication Microservice")
public class IndexController {

    @GetMapping(value = "/authentication/helloAuthentication")
    @Operation(
            summary = "Service status",
            description = "Returns a message confirming that the Authentication Microservice is running."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Service is running")
    })
    public ResponseEntity<String> getStatus() {
        return new ResponseEntity<>("Energy Management System Service - AUTHENTICATION MICROSERVICE - is running...", HttpStatus.OK);
    }
}
