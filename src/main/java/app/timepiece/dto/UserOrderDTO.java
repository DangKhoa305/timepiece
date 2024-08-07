package app.timepiece.dto;

import lombok.*;

import java.util.Date;
import java.util.List;

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
    private WatchSellerDTO watch;
    private UserDTO seller;
    private UserDTO buyer;
    private String buyeraddress;
    private String paymentMethod;
}
