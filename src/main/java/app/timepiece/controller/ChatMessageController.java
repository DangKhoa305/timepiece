package app.timepiece.controller;

import app.timepiece.dto.ChatMessage;
import app.timepiece.dto.ErrorResponse;
import app.timepiece.entity.Message;
import app.timepiece.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class ChatMessageController {

//    @MessageMapping("/chat.sendMessage/{conversationId}")
//    @SendTo("/topic/conversation/{conversationId}")
//    public ChatMessage sendMessage(ChatMessage chatMessage) {
//        return chatMessage;
//    }
//
//    @MessageMapping("/chat.addUser/{conversationId}")
//    @SendTo("/topic/conversation/{conversationId}")
//    public ChatMessage addUser(ChatMessage chatMessage) {
//        chatMessage.setContent(chatMessage.getSender() + " joined!");
//        return chatMessage;
//    }

    @Autowired
    private MessageService messageService;

    @MessageMapping("/chat.sendMessage/{conversationId}")
    @SendTo("/topic/conversation/{conversationId}")
    public Object sendMessage(ChatMessage chatMessage, @DestinationVariable Long conversationId) {
        // Lưu tin nhắn vào cơ sở dữ liệu
        try {
            Message savedMessage = messageService.sendMessage(
                    chatMessage.getSenderId(),
                    chatMessage.getRecipientId(),
                    chatMessage.getContent(),
                    conversationId
            );


            // Cập nhật chatMessage với thông tin từ savedMessage
            chatMessage.setId(savedMessage.getId());
            chatMessage.setSentAt(savedMessage.getSentAt());
            chatMessage.setStatus(savedMessage.getStatus());

            return chatMessage;
        }catch (RuntimeException e) {
             ErrorResponse errorResponse = new ErrorResponse();
             errorResponse.setMessage("Failed to send message: " + e.getMessage());
            return errorResponse;
        }
    }

    @MessageMapping("/chat.addUser/{conversationId}")
    @SendTo("/topic/conversation/{conversationId}")
    public ChatMessage addUser(ChatMessage chatMessage) {
        chatMessage.setContent(chatMessage.getSenderName() + " joined!");
        return chatMessage;
    }
}
