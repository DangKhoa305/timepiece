package app.timepiece.service.serviceImpl;


import app.timepiece.entity.Conversation;
import app.timepiece.entity.Message;
import app.timepiece.entity.User;
import app.timepiece.repository.MessageRepository;
import app.timepiece.repository.UserRepository;
import app.timepiece.service.ConversationService;
import app.timepiece.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class MessageServiceImpl implements MessageService {
    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private ConversationService conversationService;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Message sendMessage(Long senderId, Long recipientId, String messageText) {
        Conversation conversation = conversationService.getOrCreateConversation(senderId, recipientId);
        User sender = userRepository.findById(senderId).orElseThrow(() -> new RuntimeException("Sender not found"));

        Message message = Message.builder()
                .conversation(conversation)
                .sender(sender)
                .messageText(messageText)
                .sentAt(new Date())
                .status("SENT")
                .build();

        return messageRepository.save(message);
    }
}