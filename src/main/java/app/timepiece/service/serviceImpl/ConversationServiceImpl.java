package app.timepiece.service.serviceImpl;

import app.timepiece.dto.ConversationDTO;
import app.timepiece.entity.Conversation;
import app.timepiece.entity.User;
import app.timepiece.entity.Watch;
import app.timepiece.entity.WatchImage;
import app.timepiece.repository.ConversationRepository;
import app.timepiece.repository.UserRepository;
import app.timepiece.repository.WatchRepository;
import app.timepiece.service.ConversationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ConversationServiceImpl implements ConversationService {
    @Autowired
    private ConversationRepository conversationRepository;
    @Autowired
    private WatchRepository watchRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public ConversationDTO getOrCreateConversation(Long senderId, Long recipientId, Long watchId) {
        Optional<Conversation> conversationOpt = conversationRepository.findBySenderIdAndRecipientId(senderId, recipientId);
        Watch watch = watchRepository.findById(watchId).orElseThrow(() -> new RuntimeException("Watch not found"));
        if (!watch.getUser().getId().equals(recipientId)) {
            throw new RuntimeException("The recipient is not the seller of the watch");
        }
            Conversation conversation;
            if (conversationOpt.isPresent()) {
                conversation = conversationOpt.get();
            } else {

                User sender = userRepository.findById(senderId).orElseThrow(() -> new RuntimeException("Sender not found"));
                User recipient = userRepository.findById(recipientId).orElseThrow(() -> new RuntimeException("Recipient not found"));
                conversation = Conversation.builder()
                        .sender(sender)
                        .recipient(recipient)
                        .watch(watch)
                        .createdAt(new Date())
                        .build();
                conversation = conversationRepository.save(conversation);
            }

            String imageUrl = conversation.getWatch().getImages().stream()
                    .map(WatchImage::getImageUrl)
                    .findFirst()
                    .orElse(null);

        ConversationDTO conversationDTO = new ConversationDTO();
        conversationDTO.setConversationId(conversation.getId());
        conversationDTO.setWatchId(conversation.getWatch().getId());
        conversationDTO.setWatchName(conversation.getWatch().getName());
        conversationDTO.setAddress(conversation.getWatch().getAddress());
        conversationDTO.setWatchImage(imageUrl);
        conversationDTO.setRecipientId(conversation.getRecipient().getId());
        conversationDTO.setSenderId(conversation.getSender().getId());
        conversationDTO.setRecipientAvatar(conversation.getRecipient().getAvatar());
        conversationDTO.setRecipientName(conversation.getRecipient().getName());
        conversationDTO.setSenderName(conversation.getSender().getName());
        conversationDTO.setSenderAvatar(conversation.getSender().getAvatar());

        return conversationDTO;

        }



    @Override
    public List<ConversationDTO> getConversationsByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Conversation> conversations = conversationRepository.findByUser(user);

        return conversations.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private ConversationDTO convertToDTO(Conversation conversation) {

        String imageUrl = conversation.getWatch().getImages().stream()
                .map(WatchImage::getImageUrl)
                .findFirst()
                .orElse(null);

        return ConversationDTO.builder()
                .conversationId(conversation.getId())
                .watchId(conversation.getWatch().getId())
                .watchName(conversation.getWatch().getName())
                .watchImage(imageUrl)
                .address(conversation.getWatch().getAddress())
                .senderId(conversation.getSender().getId())
                .senderName(conversation.getSender().getName())
                .senderAvatar(conversation.getSender().getAvatar())
                .recipientId(conversation.getRecipient().getId())
                .recipientName(conversation.getRecipient().getName())
                .recipientAvatar(conversation.getRecipient().getAvatar())
                .build();
    }
    }


