package com.substring.chat.dto;

import lombok.Data;

@Data
public class MessageRequest {
    private String receiverId;
    private String content;
}