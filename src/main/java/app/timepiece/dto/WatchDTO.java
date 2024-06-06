package app.timepiece.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
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