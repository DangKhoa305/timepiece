package app.timepiece.repository;

import app.timepiece.entity.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, Long> {
    Optional<Conversation> findBySenderIdAndRecipientId(Long senderId, Long recipientId);
}
