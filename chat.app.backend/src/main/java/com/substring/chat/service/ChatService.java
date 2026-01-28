package com.substring.chat.service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.substring.chat.dto.ChatResponse;
import com.substring.chat.model.Chat;
import com.substring.chat.repository.ChatRepository;

@Service
public class ChatService {

    @Autowired
    private ChatRepository chatRepository;

    public Chat createOrGetChat(String user1Id, String user2Id) {
        List<Chat> existingChats = chatRepository.findByParticipantsWithAll(Arrays.asList(user1Id, user2Id));
        
        // Filter chats that contain exactly both participants
        for (Chat chat : existingChats) {
            if (chat.getParticipants().contains(user1Id) && 
                chat.getParticipants().contains(user2Id) && 
                chat.getParticipants().size() == 2) {
                return chat;
            }
        }

        Chat chat = new Chat();
        chat.setParticipants(List.of(user1Id, user2Id));
        chat.setChatType(Chat.ChatType.PRIVATE);
        chat.setCreatedAt(LocalDateTime.now());
        
        return chatRepository.save(chat);
    }

    public List<ChatResponse> getChatsForUser(String userId) {
        List<Chat> chats = chatRepository.findByParticipantsContaining(userId);
        
        return chats.stream()
                .map(ChatResponse::fromChat)
                .collect(Collectors.toList());
    }

    public Chat getChatById(String chatId) {
        return chatRepository.findById(chatId)
                .orElseThrow(() -> new RuntimeException("Chat not found"));
    }

    public boolean isUserParticipantOfChat(String chatId, String userId) {
        Chat chat = getChatById(chatId);
        return chat.getParticipants().contains(userId);
    }
}