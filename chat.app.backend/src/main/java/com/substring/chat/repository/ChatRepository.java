package com.substring.chat.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.substring.chat.model.Chat;

@Repository
public interface ChatRepository extends MongoRepository<Chat, String> {
    List<Chat> findByParticipantsContaining(String userId);
    @Query("{ 'participants' : { $all: ?0 } }")
    List<Chat> findByParticipantsWithAll(List<String> participants);
}