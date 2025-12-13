package com.example.websocketmicroservice.controllers;

import com.example.websocketmicroservice.services.ChatService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import java.util.UUID;

@Controller
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    /**
     * User sends a chat message and server receives it here.
     * The userId is stored in WebSocket session attributes by JwtHandshakeInterceptor.
     */
    @MessageMapping("/chat.send")
    public void handleUserMessage(
            @Payload String content,
            SimpMessageHeaderAccessor headerAccessor
    ) {
        UUID userId = (UUID) headerAccessor.getSessionAttributes().get("userId");
        String username = (String) headerAccessor.getSessionAttributes().get("username");
        chatService.handleUserMessage(userId, username, content);
    }

    /**
     * Admin sends a reply.
     * The admin provides userId in message header "userId".
     */
    @MessageMapping("/chat.adminReply")
    public void handleAdminReply(
            @Header("userId") String userIdString,
            @Payload String content,
            SimpMessageHeaderAccessor headerAccessor
    ) {
        UUID userId = UUID.fromString(userIdString);
        String username = (String) headerAccessor.getSessionAttributes().get("username");
        chatService.handleAdminMessage(userId, username, content);
    }

    /**
     * AI Support: user requests an AI-generated response.
     */
    @MessageMapping("/chat.aiRequest")
    public void handleAiRequest(
            @Payload String content,
            SimpMessageHeaderAccessor headerAccessor
    ) {
        UUID userId = (UUID) headerAccessor.getSessionAttributes().get("userId");
        String username = (String) headerAccessor.getSessionAttributes().get("username");
        chatService.handleAiMessage(userId, username, content);
    }

    @MessageMapping("/chat.waitForAdmin")
    public void waitForAdmin(
            @Payload String content,
            SimpMessageHeaderAccessor headerAccessor
    ) {
        UUID userId = (UUID) headerAccessor.getSessionAttributes().get("userId");
        String username = (String) headerAccessor.getSessionAttributes().get("username");

        chatService.notifyAdminOnly(userId, username, content);
    }

}
