package app.timepiece.service;

import app.timepiece.dto.MessageDTO;
import app.timepiece.entity.Message;

import java.util.List;

public interface MessageService {
    Message sendMessage(Long senderId, Long recipientId, String messageText, Long conversationId);
    List<MessageDTO> getMessagesByConversationId(Long conversationId);
}
