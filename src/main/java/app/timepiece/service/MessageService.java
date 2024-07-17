package app.timepiece.service;

import app.timepiece.entity.Message;

public interface MessageService {
    Message sendMessage(Long senderId, Long recipientId, String messageText);
}
