package com.substring.chat.dto;

import lombok.Data;

@Data
public class AuthResponse {
    private String token;
    private String userId;
    private String name;
    private String email;
}