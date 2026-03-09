package com.substring.chat.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.substring.chat.model.Message;

@Service
public class SocketIoService {

    @Autowired
    private SocketIOServer socketIOServer;

    public void emitMessageToUser(String userId, Message message) {
        for (SocketIOClient client : socketIOServer.getAllClients()) {
            String clientId = (String) client.get("userId");
            if (userId != null && userId.equals(clientId)) {
                client.sendEvent("receive_message", message);
                break;
            }
        }
    }

    public void emitMessageToChat(String chatId, Message message) {
        socketIOServer.getRoomOperations(chatId).sendEvent("receive_message", message);
    }

    public void emitToSpecificClient(String sessionId, String eventName, Object data) {
    }
}