package app.timepiece.controller;

import app.timepiece.dto.ChatMessage;
import app.timepiece.dto.MessageDTO;
import app.timepiece.entity.Message;
import app.timepiece.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

//    @PostMapping("/send")
//    public Message sendMessage(@RequestParam Long senderId, @RequestParam Long recipientId, @RequestParam String messageText) {
//        Message message = messageService.sendMessage(senderId, recipientId, messageText);
//
//        // Gửi tin nhắn qua WebSocket để realtime chat
//        ChatMessage chatMessage = new ChatMessage();
//        chatMessage.setSender(message.getSender().getName());
//        chatMessage.setContent(message.getMessageText());
//        chatMessage.setType(ChatMessage.MessageType.CHAT);
//
//        messagingTemplate.convertAndSend("/topic/messages/" + recipientId, chatMessage);
//
//        return message;
//    }

    @GetMapping("/{conversationId}/messages")
    public ResponseEntity<?> getMessagesByConversationId(@PathVariable Long conversationId) {
        try {
            List<MessageDTO> messages = messageService.getMessagesByConversationId(conversationId);
            return ResponseEntity.ok(messages);
        } catch (RuntimeException e) {
            // Handle specific exception or return generic error
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            // Handle any other exceptions
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred");
        }
    }
}
