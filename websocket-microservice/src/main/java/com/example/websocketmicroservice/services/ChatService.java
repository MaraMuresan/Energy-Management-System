package com.example.websocketmicroservice.services;

import com.example.websocketmicroservice.dtos.ChatMessageDTO;
import com.example.websocketmicroservice.dtos.ChatResponseDTO;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class ChatService {

    private final RuleEngineService ruleEngineService;
    private final SimpMessagingTemplate messagingTemplate;
    private final AiService aiService;

    public ChatService(RuleEngineService ruleEngineService, SimpMessagingTemplate messagingTemplate, AiService aiService) {
        this.ruleEngineService = ruleEngineService;
        this.messagingTemplate = messagingTemplate;
        this.aiService = aiService;
    }

    /**
     * Handles messages sent by the user through /app/chat.send
     */
    public void handleUserMessage(UUID userId, String username, String content) {
        ChatMessageDTO adminViewMessage = new ChatMessageDTO(
                userId,
                username,
                "USER",
                content,
                LocalDateTime.now()
        );

        messagingTemplate.convertAndSend("/topic/chat/admin", adminViewMessage);

        String autoReply = ruleEngineService.matchRule(content);

        if (autoReply != null) {
            ChatResponseDTO botResponse = new ChatResponseDTO(
                    userId,
                    username,
                    "BOT",
                    autoReply,
                    LocalDateTime.now()
            );

            messagingTemplate.convertAndSend("/topic/chat/user/" + userId, botResponse);
            messagingTemplate.convertAndSend("/topic/chat/admin", botResponse);
        }
    }

    /**
     * Handles messages sent by the admin through /app/chat.adminReply
     */
    public void handleAdminMessage(UUID userId, String username, String content) {

        ChatResponseDTO reply = new ChatResponseDTO(
                userId,
                username,
                "ADMIN",
                content,
                LocalDateTime.now()
        );

        messagingTemplate.convertAndSend("/topic/chat/user/" + userId, reply);
    }

    /**
     * Handles AI-generated responses requested by the user through /app/chat.aiRequest
     */
    public void handleAiMessage(UUID userId, String username, String content) {

        String aiReply = aiService.askAI(content);

        ChatResponseDTO response = new ChatResponseDTO(
                userId,
                username,
                "AI",
                aiReply,
                LocalDateTime.now()
        );

        messagingTemplate.convertAndSend("/topic/chat/user/" + userId, response);
        messagingTemplate.convertAndSend("/topic/chat/admin", response);
    }

    public void notifyAdminOnly(UUID userId, String username, String content) {

        ChatMessageDTO adminViewMessage = new ChatMessageDTO(
                userId,
                username,
                "USER",
                content,
                LocalDateTime.now()
        );

        //messagingTemplate.convertAndSend("/topic/chat/admin", adminViewMessage);
    }
}
