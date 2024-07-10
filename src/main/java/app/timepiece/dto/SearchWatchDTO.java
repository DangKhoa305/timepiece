package app.timepiece.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchWatchDTO {
    private Long id;
    private String name;
    private String watchstatus;
    private double price;
    private String watchImage;
    private String accessories;
    private String status;
    private String imageUrl;


}
