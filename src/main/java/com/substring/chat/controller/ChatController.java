package com.substring.chat.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.substring.chat.dto.ChatResponse;
import com.substring.chat.service.ChatService;

@RestController
@RequestMapping("/api/chats")
@CrossOrigin(origins = "*")
public class ChatController {

    @Autowired
    private ChatService chatService;

    @GetMapping
    public ResponseEntity<List<ChatResponse>> getUserChats(Authentication authentication) {
        String userId = authentication.getName();
        List<ChatResponse> chats = chatService.getChatsForUser(userId);
        return ResponseEntity.ok(chats);
    }

    @GetMapping("/{chatId}")
    public ResponseEntity<ChatResponse> getChatById(@PathVariable String chatId, Authentication authentication) {
        String userId = authentication.getName();
        
        if (!chatService.isUserParticipantOfChat(chatId, userId)) {
            return ResponseEntity.status(403).build();
        }
        
        ChatResponse response = ChatResponse.fromChat(chatService.getChatById(chatId));
        return ResponseEntity.ok(response);
    }
}