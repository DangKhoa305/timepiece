package app.timepiece.service;

import app.timepiece.entity.Conversation;

public interface ConversationService {
    Conversation getOrCreateConversation(Long senderId, Long recipientId);
}
