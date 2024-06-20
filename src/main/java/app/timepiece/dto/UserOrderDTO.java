package app.timepiece.dto;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserOrderDTO {
    private Long id;
    private String note;
    private Date orderDate;
    private double totalPrice;
    private String status;
    private String watchName;
}
