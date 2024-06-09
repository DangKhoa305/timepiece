package app.timepiece.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class AppraiserRequestStatusDTO {
    private Long id;
    private String status;

}
