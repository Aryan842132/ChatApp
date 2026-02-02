package com.substring.chat.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnDisconnect;
import com.corundumstudio.socketio.annotation.OnEvent;
import com.substring.chat.dto.MessageRequest;
import com.substring.chat.model.Message;
import com.substring.chat.security.JwtUtil;
import com.substring.chat.service.MessageService;

@Component
public class SocketIoEventHandler {

    @Autowired
    private SocketIOServer socketIOServer;

    @Autowired
    private MessageService messageService;

    @Autowired
    private JwtUtil jwtUtil;

    @OnConnect
    public void onConnect(SocketIOClient client) {
        String token = client.getHandshakeData().getSingleUrlParam("token");
        if (token != null) {
            try {
                String userId = jwtUtil.extractUserId(token);
                if (jwtUtil.validateToken(token, userId)) {
                    client.set("userId", userId);
                    System.out.println("Socket.IO Client connected: " + userId);
                } else {
                    client.disconnect();
                }
            } catch (Exception e) {
                client.disconnect();
            }
        } else {
            client.disconnect();
        }
    }

    @OnDisconnect
    public void onDisconnect(SocketIOClient client) {
        String userId = (String) client.get("userId");
        System.out.println("Socket.IO Client disconnected: " + userId);
    }

    @OnEvent("send_message")
    public void onSendMessage(SocketIOClient client, MessageRequest messageRequest) {
        String senderId = (String) client.get("userId");
        if (senderId != null) {
            Message savedMessage = messageService.sendMessage(senderId, messageRequest);

            String recipientId = messageRequest.getReceiverId();
            SocketIOClient recipientClient = findClientByUserId(recipientId);
            
            if (recipientClient != null) {
                recipientClient.sendEvent("receive_message", savedMessage);
            }
            
            client.sendEvent("receive_message", savedMessage);
        }
    }

    @OnEvent("join_chat")
    public void onJoinChat(SocketIOClient client, String chatId) {
        String userId = (String) client.get("userId");
        if (userId != null) {
            client.joinRoom(chatId);
            System.out.println("User " + userId + " joined chat " + chatId);
        }
    }

    @OnEvent("leave_chat")
    public void onLeaveChat(SocketIOClient client, String chatId) {
        String userId = (String) client.get("userId");
        if (userId != null) {
            client.leaveRoom(chatId);
            System.out.println("User " + userId + " left chat " + chatId);
        }
    }

    private SocketIOClient findClientByUserId(String userId) {
        for (SocketIOClient client : socketIOServer.getAllClients()) {
            String clientId = (String) client.get("userId");
            if (userId != null && userId.equals(clientId)) {
                return client;
            }
        }
        return null;
    }
}