package com.substring.chat.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserLoginRequest {
    @NotBlank(message = "Email or mobile is required")
    private String emailOrMobile;
    
    @NotBlank(message = "Password is required")
    private String password;
}