package com.substring.chat.dto;

import com.substring.chat.model.Chat;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ChatResponse {
    private String chatId;
    private List<String> participants;
    private Chat.ChatType chatType;
    private LocalDateTime createdAt;
    private String lastMessage;
    private LocalDateTime lastMessageTime;
    
    public static ChatResponse fromChat(Chat chat) {
        ChatResponse response = new ChatResponse();
        response.setChatId(chat.getChatId());
        response.setParticipants(chat.getParticipants());
        response.setChatType(chat.getChatType());
        response.setCreatedAt(chat.getCreatedAt());
        return response;
    }
}