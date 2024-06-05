package app.timepiece.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShowWatchDTO {
    private Long id;
    private String name;
    private double price;
    private String status;
    private String imageUrl;

}
