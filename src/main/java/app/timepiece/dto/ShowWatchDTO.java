package app.timepiece.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShowWatchDTO {
    private Long id;
    private String name;
    private double price;
    private String status;
    private String imageUrl;

}
