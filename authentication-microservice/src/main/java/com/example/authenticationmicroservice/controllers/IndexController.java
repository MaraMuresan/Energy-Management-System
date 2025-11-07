package com.example.authenticationmicroservice.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@CrossOrigin
public class IndexController {

    @GetMapping(value = "/authentication/helloAuthentication")
    public ResponseEntity<String> getStatus() {
        return new ResponseEntity<>("Energy Management System Service - AUTHENTICATION MICROSERVICE - is running...", HttpStatus.OK);
    }
}
