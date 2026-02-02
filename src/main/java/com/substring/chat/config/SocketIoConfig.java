package com.substring.chat.config;

import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.AuthorizationListener;
import com.corundumstudio.socketio.HandshakeData;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SocketIoConfig {

    @Value("${socketio.port:9092}")
    private int socketIoPort;

    @Bean
    public SocketIOServer socketIOServer() {
        com.corundumstudio.socketio.Configuration config = new com.corundumstudio.socketio.Configuration();
        config.setHostname("0.0.0.0");  
        config.setPort(socketIoPort);    
        
        config.setAuthorizationListener(new AuthorizationListener() {
            @Override
            public boolean isAuthorized(HandshakeData handshakeData) {
                String token = handshakeData.getSingleUrlParam("token");
                return token != null && !token.isEmpty();
            }
        });

        return new SocketIOServer(config);
    }
}