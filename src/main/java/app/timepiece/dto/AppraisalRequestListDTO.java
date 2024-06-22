package app.timepiece.dto;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class AppraisalRequestListDTO {
    private Long id;
    private Date createDate;
    private String brand;
    private String status;
    private Long code;
    private Date updateDate;
}