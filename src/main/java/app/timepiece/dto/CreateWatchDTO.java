package app.timepiece.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateWatchDTO {
    @NotNull(message = "User ID cannot be null")
    private Long userId;

    @NotBlank(message = "Name cannot be blank")
    @Size(max = 100, message = "Name cannot exceed 100 characters")
    private String name;

    @NotBlank(message = "Watch status cannot be blank")
    private String watchStatus;

    @NotBlank(message = "Description cannot be blank")
    private String description;

    @Positive(message = "Price must be positive")
    private double price;

    @NotNull(message = "Brand ID cannot be null")
    private Long brandId;

    @PositiveOrZero(message = "Year produced must be zero or positive")
    @Min(value = 100, message = "Year produced cannot be before 100")
    private int yearProduced;

    @NotBlank(message = "Model cannot be blank")
    private String model;

    @NotBlank(message = "Material cannot be blank")
    private String material;

    @NotBlank(message = "Watch strap cannot be blank")
    private String watchStrap;

    @NotBlank(message = "Size cannot be blank")
    private String size;

    @NotBlank(message = "Accessories cannot be blank")
    private String accessories;

    @NotBlank(message = "Reference code cannot be blank")
    private String referenceCode;

    @NotBlank(message = "Place of production cannot be blank")
    private String placeOfProduction;

    @NotNull(message = "Watch type ID cannot be null")
    private Long watchTypeId;

    @NotBlank(message = "Address cannot be blank")
    private String address;

    @NotEmpty(message = "Image files cannot be empty")
    private List<MultipartFile> imageFiles;
}
