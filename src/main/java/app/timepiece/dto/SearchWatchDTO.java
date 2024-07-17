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
    private Long sellerId;
    private String sellerName;
    private String sellerImage;
    private String watchImage;
    private String accessories;
    private String status;
    private String area;
    private String imageUrl;


}
