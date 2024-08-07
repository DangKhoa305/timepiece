package app.timepiece.service.serviceImpl;


import app.timepiece.dto.MessageDTO;
import app.timepiece.entity.Conversation;
import app.timepiece.entity.Message;
import app.timepiece.entity.User;
import app.timepiece.repository.ConversationRepository;
import app.timepiece.repository.MessageRepository;
import app.timepiece.repository.UserRepository;
import app.timepiece.service.ConversationService;
import app.timepiece.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MessageServiceImpl implements MessageService {
    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private ConversationRepository conversationRepository;

    @Autowired
    private ConversationService conversationService;

    @Autowired
    private UserRepository userRepository;


    @Override
    public Message sendMessage(Long senderId, Long recipientId, String messageText, Long conversationId) {

        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new RuntimeException("Conversation not found"));

        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new RuntimeException("Sender not found"));

        Message message = Message.builder()
                .conversation(conversation)
                .sender(sender)
                .messageText(messageText)
                .sentAt(new Date())
                .status("SENT")
                .build();

        return messageRepository.save(message);
    }

    @Override
    public List<MessageDTO> getMessagesByConversationId(Long conversationId) {
        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new RuntimeException("Conversation not found"));

        List<Message> messages = messageRepository.findByConversation(conversation);

        return messages.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private MessageDTO convertToDTO(Message message) {
        return MessageDTO.builder()
                .id(message.getId())
                .senderId(message.getSender().getId())
                .senderName(message.getSender().getName())
                .senderAvatar(message.getSender().getAvatar())
                .messageText(message.getMessageText())
                .sentAt(message.getSentAt())
                .status(message.getStatus())
                .build();
    }
}