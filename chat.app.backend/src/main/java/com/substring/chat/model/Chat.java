package com.substring.chat.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Document(collection = "chats")
public class Chat {
    @Id
    private String chatId;
    
    private List<String> participants;
    
    private ChatType chatType;
    
    private LocalDateTime createdAt;
    
    public enum ChatType {
        PRIVATE,
        GROUP
    }
}