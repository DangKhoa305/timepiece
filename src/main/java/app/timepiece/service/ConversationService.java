package app.timepiece.service;

import app.timepiece.dto.ConversationDTO;
import app.timepiece.entity.Conversation;

import java.util.List;

public interface ConversationService {
    ConversationDTO getOrCreateConversation(Long senderId, Long recipientId, Long watchId);
    List<ConversationDTO> getConversationsByUserId(Long userId);


}
