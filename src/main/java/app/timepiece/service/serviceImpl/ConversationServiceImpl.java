package app.timepiece.service.serviceImpl;

import app.timepiece.entity.Conversation;
import app.timepiece.entity.User;
import app.timepiece.repository.ConversationRepository;
import app.timepiece.repository.UserRepository;
import app.timepiece.service.ConversationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class ConversationServiceImpl implements ConversationService {

    @Autowired
    private ConversationRepository conversationRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Conversation getOrCreateConversation(Long senderId, Long recipientId) {
        Optional<Conversation> conversationOpt = conversationRepository.findBySenderIdAndRecipientId(senderId, recipientId);
        if (conversationOpt.isPresent()) {
            return conversationOpt.get();
        }

        User sender = userRepository.findById(senderId).orElseThrow(() -> new RuntimeException("Sender not found"));
        User recipient = userRepository.findById(recipientId).orElseThrow(() -> new RuntimeException("Recipient not found"));

        Conversation conversation = Conversation.builder()
                .sender(sender)
                .recipient(recipient)
                .createdAt(new Date())
                .build();

        return conversationRepository.save(conversation);
    }
}
