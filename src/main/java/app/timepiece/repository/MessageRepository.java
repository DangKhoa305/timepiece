package app.timepiece.repository;

import app.timepiece.entity.Conversation;
import app.timepiece.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByConversation(Conversation conversation);
}
