package app.timepiece.dto;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateFeedbackDTO {
    private String comment;
    private Long orderId;
    private Long parentFeedbackId;
}
