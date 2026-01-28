package com.substring.chat.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.substring.chat.dto.MessageRequest;
import com.substring.chat.dto.MessageResponse;
import com.substring.chat.model.Message;
import com.substring.chat.service.MessageService;

@RestController
@RequestMapping("/api/messages")
@CrossOrigin(origins = "*")
public class MessageController {

    @Autowired
    private MessageService messageService;
    
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @PostMapping("/send")
    public ResponseEntity<MessageResponse> sendMessage(
            @RequestBody MessageRequest request,
            Authentication authentication) {
        
        String senderId = authentication.getName();
        Message message = messageService.sendMessage(senderId, request);
        MessageResponse response = MessageResponse.fromMessage(message);
        
        // Publish message to WebSocket for real-time delivery
        messagingTemplate.convertAndSend("/topic/chat/" + message.getChatId(), response);
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/chat/{chatId}")
    public ResponseEntity<List<MessageResponse>> getChatMessages(
            @PathVariable String chatId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size,
            Authentication authentication) {
        
        String userId = authentication.getName();
        List<MessageResponse> messages = messageService.getChatHistory(chatId, userId, page, size);
        
        return ResponseEntity.ok(messages);
    }

    @GetMapping("/chat/{chatId}/all")
    public ResponseEntity<List<MessageResponse>> getAllChatMessages(
            @PathVariable String chatId,
            Authentication authentication) {
        
        String userId = authentication.getName();
        List<MessageResponse> messages = messageService.getAllMessagesInChat(chatId, userId);
        
        return ResponseEntity.ok(messages);
    }

    @PutMapping("/{messageId}/status")
    public ResponseEntity<Void> updateMessageStatus(
            @PathVariable String messageId,
            @RequestParam Message.MessageStatus status) {
        
        messageService.updateMessageStatus(messageId, status);
        return ResponseEntity.ok().build();
    }
}