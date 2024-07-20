package app.timepiece.dto;


import lombok.*;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class WatchSellerDTO {
    private String imageUrl;
    private String name;
    private String size;
    private double price;
    private Date createDate;
    private String address;
    private String area;
    private String status;
    private  Date startDate;
    private String typePost;
    private int numberDatePost;
    private Date endDate;
}