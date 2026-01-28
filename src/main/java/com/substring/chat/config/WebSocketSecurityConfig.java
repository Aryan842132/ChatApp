package com.substring.chat.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageInterceptor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.socket.messaging.StompSubProtocolErrorHandler;

@Configuration
public class WebSocketSecurityConfig extends AbstractSecurityWebSocketMessageInterceptor {

    @Override
    protected Authentication getAuthenticatedUser(org.springframework.web.socket.messaging.SessionConnectedEvent sessionConnectedEvent) {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    @Override
    protected boolean isUserCreatedAuthentication(Authentication authentication) {
        return authentication != null && authentication.isAuthenticated();
    }
}