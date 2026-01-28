package com.substring.chat.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "messages")
public class Message {
    @Id
    private String messageId;
    
    @Indexed
    private String chatId;
    
    private String senderId;
    
    private String receiverId;
    
    private String content;
    
    private LocalDateTime timestamp;
    
    private MessageStatus status;
    
    public enum MessageStatus {
        SENT,
        DELIVERED,
        READ
    }
}