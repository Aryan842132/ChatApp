package com.substring.chat.websocket;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.substring.chat.dto.MessageRequest;
import com.substring.chat.dto.MessageResponse;
import com.substring.chat.model.Message;
import com.substring.chat.service.MessageService;

@Controller
public class ChatWebSocketController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private MessageService messageService;

    @MessageMapping("/sendMessage")
    public void sendMessage(@Payload MessageRequest messageRequest, Principal principal) {
        
        String senderId = principal.getName();
        
        Message savedMessage = messageService.sendMessage(senderId, messageRequest);
        
        MessageResponse response = MessageResponse.fromMessage(savedMessage);
        
        messagingTemplate.convertAndSend("/topic/chat/" + savedMessage.getChatId(), response);
    }
}