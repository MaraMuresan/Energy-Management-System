package com.example.authenticationmicroservice.dtos.builders;

import com.example.authenticationmicroservice.dtos.JwtResponseDTO;

public class JwtResponseBuilder {

    private JwtResponseBuilder() {}

    public static JwtResponseDTO toJwtResponseDTO(String token, String role) {
        JwtResponseDTO jwtResponse = new JwtResponseDTO();
        jwtResponse.setToken(token);
        jwtResponse.setRole(role);
        return jwtResponse;
    }
}
