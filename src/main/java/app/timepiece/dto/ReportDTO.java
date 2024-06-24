package app.timepiece.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReportDTO {

    @NotBlank(message = "Brand is required")
    private String brand;

    @NotBlank(message = "Model is required")
    private String model;

    @NotBlank(message = "Reference code is required")
    private String referenceCode;

    @NotBlank(message = "Watch type is required")
    private String watchType;

    @NotBlank(message = "Material is required")
    private String material;

    @NotBlank(message = "Watch strap is required")
    private String watchStrap;

    @Min(value = 1574, message = "Year produced should be no less than 1900")
    @Max(value = 2100, message = "Year produced should be no more than 2100")
    private int yearProduced;

    @NotBlank(message = "Watch status is required")
    private String watchStatus;

    @NotBlank(message = "Accessories are required")
    private String accessories;

    @NotBlank(message = "Origin is required")
    private String origin;

    @NotBlank(message = "Size is required")
    private String size;

    @NotNull(message = "User ID is required")
    private Long userId;

    private String commentValue;

    @NotEmpty(message = "At least one image file is required")
    private List<MultipartFile> imageFiles;

}