package app.timepiece.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

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

    @NotNull(message = "Year produced is required")
    private int yearProduced;

    @NotBlank(message = "Watch status is required")
    private String watchStatus;

    private String accessories;

    private String origin;

    @NotBlank(message = "Size is required")
    private String size;

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotBlank(message = "Comment value is required")
    private String commentValue;

    @NotNull(message = "Image files are required")
    private List<MultipartFile> imageFiles;

//    @NotNull(message = "Appraisal request ID is required")
//    private Long appraisalRequestId;
}
