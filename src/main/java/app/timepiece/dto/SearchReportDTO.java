package app.timepiece.dto;


import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchReportDTO {
        private Long id;
        private String brand;
        private String reportStatus;
        private Date createDate;

    }
