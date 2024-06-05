package app.timepiece.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WatchDTO {

    private Long id;
    private String name;
    private String status;
    private String description;
    private double price;
    private String brandName;
    private int yearProduced;
    private String model;
    private String material;
    private String watchStrap;
    private String size;
    private String accessories;
    private String referenceCode;
    private String watchTypeName;
    private List<String> imageUrls;


}