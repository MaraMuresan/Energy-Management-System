package com.example.websocketmicroservice.dtos;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class ChatResponseDTO {

    private UUID userId;
    private String username;
    private String from;
    private String content;
    private LocalDateTime timestamp;

    public ChatResponseDTO() {
    }

    public ChatResponseDTO(UUID userId, String username, String from, String content, LocalDateTime timestamp) {
        this.userId = userId;
        this.username = username;
        this.from = from;
        this.content = content;
        this.timestamp = timestamp;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ChatResponseDTO)) return false;
        ChatResponseDTO that = (ChatResponseDTO) o;
        return Objects.equals(userId, that.userId) &&
                Objects.equals(username, that.username) &&
                Objects.equals(from, that.from) &&
                Objects.equals(content, that.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, username, from, content);
    }
}
