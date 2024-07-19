package app.timepiece.dto;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMessage {
//    private String sender;
//    private String content;
//    private MessageType type;
//
//    public enum MessageType {
//        CHAT,
//        JOIN,
//        LEAVE
//    }

    private Long id;
    private Long senderId;
    private String senderName;
    private Long recipientId;
    private String recipientName;
    private String content; // nội dung tin nhắn
    private Date sentAt;
    private String status;
    private Long conversationId;

}
