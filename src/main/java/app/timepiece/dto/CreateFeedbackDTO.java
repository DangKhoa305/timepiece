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
    private Long userId;
    private Long parentFeedbackId;
    private Double rating;
}
