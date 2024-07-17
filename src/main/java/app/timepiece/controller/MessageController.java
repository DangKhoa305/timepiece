package app.timepiece.controller;

import app.timepiece.dto.ChatMessage;
import app.timepiece.entity.Message;
import app.timepiece.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @PostMapping("/send")
    public Message sendMessage(@RequestParam Long senderId, @RequestParam Long recipientId, @RequestParam String messageText) {
        Message message = messageService.sendMessage(senderId, recipientId, messageText);

        // Gửi tin nhắn qua WebSocket để realtime chat
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setSender(message.getSender().getName());
        chatMessage.setContent(message.getMessageText());
        chatMessage.setType(ChatMessage.MessageType.CHAT);

        messagingTemplate.convertAndSend("/topic/messages/" + recipientId, chatMessage);

        return message;
    }
}
