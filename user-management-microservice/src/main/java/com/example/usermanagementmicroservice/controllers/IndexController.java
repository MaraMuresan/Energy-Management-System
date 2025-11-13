package com.example.usermanagementmicroservice.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@CrossOrigin
@Tag(name = "User Status", description = "Health check for User Microservice")
public class IndexController {

    @GetMapping(value = "/user/helloUser")
    @Operation(
            summary = "Service status",
            description = "Returns a message indicating that the User Microservice is running."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Service is running")
    })
    public ResponseEntity<String> getStatus() {
        return new ResponseEntity<>("Energy Management System Service - USER MICROSERVICE - is running...", HttpStatus.OK);
    }
}
