package app.timepiece.dto;

import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConversationDTO {
    private Long conversationId;
    private Long watchId;
    private String watchName;
    private String watchImage;
    private String address;

    private Long senderId;
    private String senderName;
    private String senderAvatar;

    private Long recipientId;
    private String recipientName;
    private String recipientAvatar;
}
