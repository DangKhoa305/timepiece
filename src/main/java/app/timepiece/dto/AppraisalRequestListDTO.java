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
    private String title;
    private String brand;
    private Date updateDate;
}