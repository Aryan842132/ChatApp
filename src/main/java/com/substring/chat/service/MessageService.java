package com.substring.chat.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.substring.chat.dto.MessageRequest;
import com.substring.chat.dto.MessageResponse;
import com.substring.chat.model.Chat;
import com.substring.chat.model.Message;
import com.substring.chat.repository.MessageRepository;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private ChatService chatService;

    public Message sendMessage(String senderId, MessageRequest request) {
        
        Chat chat = chatService.createOrGetChat(senderId, request.getReceiverId());

        Message message = new Message();
        message.setChatId(chat.getChatId());
        message.setSenderId(senderId);
        message.setReceiverId(request.getReceiverId());
        message.setContent(request.getContent());
        message.setTimestamp(LocalDateTime.now());
        message.setStatus(Message.MessageStatus.SENT);

        return messageRepository.save(message);
    }

    public List<MessageResponse> getChatHistory(String chatId, String userId, int page, int size) {
        
        if (!chatService.isUserParticipantOfChat(chatId, userId)) {
            throw new RuntimeException("User is not authorized to access this chat");
        }

        Pageable pageable = PageRequest.of(page, size);
        List<Message> messages = messageRepository.findByChatIdOrderByTimestampAsc(chatId, pageable);

        return messages.stream()
                .map(MessageResponse::fromMessage)
                .collect(Collectors.toList());
    }

    public List<MessageResponse> getAllMessagesInChat(String chatId, String userId) {
        
        if (!chatService.isUserParticipantOfChat(chatId, userId)) {
            throw new RuntimeException("User is not authorized to access this chat");
        }

        List<Message> messages = messageRepository.findByChatId(chatId);

        return messages.stream()
                .map(MessageResponse::fromMessage)
                .collect(Collectors.toList());
    }

    public void updateMessageStatus(String messageId, Message.MessageStatus status) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("Message not found"));

        message.setStatus(status);
        messageRepository.save(message);
    }

    public Message getMessageById(String messageId) {
        return messageRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("Message not found"));
    }
}