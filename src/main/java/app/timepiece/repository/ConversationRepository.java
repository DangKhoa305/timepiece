package app.timepiece.repository;

import app.timepiece.entity.Conversation;
import app.timepiece.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, Long> {
    Optional<Conversation> findBySenderIdAndRecipientId(Long senderId, Long recipientId);
    @Query("SELECT c FROM Conversation c WHERE c.sender = :user OR c.recipient = :user")
    List<Conversation> findByUser(@Param("user") User user);
}
