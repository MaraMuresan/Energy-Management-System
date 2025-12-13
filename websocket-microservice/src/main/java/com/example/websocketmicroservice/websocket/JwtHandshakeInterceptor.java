package com.example.websocketmicroservice.websocket;

import com.example.websocketmicroservice.services.JwtTokenService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;
import java.util.UUID;

@Slf4j
@Component
public class JwtHandshakeInterceptor implements HandshakeInterceptor {

    private final JwtTokenService jwtTokenService;

    public JwtHandshakeInterceptor(JwtTokenService jwtTokenService) {
        this.jwtTokenService = jwtTokenService;
    }

    @Override
    public boolean beforeHandshake(
            ServerHttpRequest request,
            ServerHttpResponse response,
            WebSocketHandler wsHandler,
            Map<String, Object> attributes
    ) {

        String token = null;

        if (request instanceof ServletServerHttpRequest servletRequest) {
            HttpServletRequest http = servletRequest.getServletRequest();

            String authHeader = http.getHeader("Authorization");
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                token = authHeader.substring(7);
            }

            if (token == null) {
                token = http.getParameter("token");
            }
        }

        if (token == null) {
            log.warn("JWT token missing in WebSocket handshake. Connection rejected");
            return false;
        }

        try {
            Claims claims = jwtTokenService.parseToken(token);
            UUID userId = UUID.fromString((String) claims.get("id"));
            String username = claims.getSubject();
            attributes.put("userId", userId);
            attributes.put("username", username);

            log.info("WebSocket connection authenticated for userId={}", userId);
            return true;

        } catch (Exception e) {
            log.error("Invalid JWT token in WebSocket handshake: {}", e.getMessage());
            return false;
        }
    }

    @Override
    public void afterHandshake(
            ServerHttpRequest request,
            ServerHttpResponse response,
            WebSocketHandler wsHandler,
            Exception exception
    ) {
        //nothing to do after handshake
    }
}
