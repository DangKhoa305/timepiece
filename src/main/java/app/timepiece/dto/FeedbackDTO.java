package app.timepiece.dto;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FeedbackDTO {
    private Long id;
    private String comment;
    private String timestamp;
    private String userName;
    private String avatar;
    private Double rating;
    private Long orderId;
    private Long parentFeedbackId;
    private List<FeedbackDTO> childFeedbacks;
}