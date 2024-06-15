package app.timepiece.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateWatchDTO {
    private Long userId;
    private String name;
    private String watchStatus;
    private String description;
    private double price;
    private Long brandId;
    private int yearProduced;
    private String model;
    private String material;
    private String watchStrap;
    private String size;
    private String accessories;
    private String referenceCode;
    private String placeOfProduction;
    private Long watchTypeId;
    private String address;
    private List<MultipartFile> imageFiles;
}
