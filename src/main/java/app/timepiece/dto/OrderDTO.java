package app.timepiece.dto;

import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDTO {
    private Long id;
    private String note;
    private Date orderDate;
    private double totalPrice;
    private Long userId;
    private String watchName;
    private String status;
    private List<String> watchImages;
    private Long sellerId;
}
