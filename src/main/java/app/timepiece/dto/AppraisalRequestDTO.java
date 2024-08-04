package app.timepiece.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppraisalRequestDTO {
    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Phone number is required")
    private String phoneNumber;

    @NotNull(message = "Has original box is required")
    private boolean hasOriginalBox;

    @NotNull(message = "Has papers or warranty is required")
    private boolean hasPapersOrWarranty;

    @NotNull(message = "Has purchase receipt is required")
    private boolean hasPurchaseReceipt;

    @NotNull(message = "Are there any stickers is required")
    private boolean areThereAnyStickers;

    @NotNull(message = "Age is required")
    private int age;

    @NotNull(message = "Desired price is required")
    private double desiredPrice;

    private String description;

    @NotBlank(message = "Brand is required")
    private String brand;

    @NotBlank(message = "Reference code is required")
    private String referenceCode;

    @NotNull(message = "Image files are required")
    private List<MultipartFile> imageFiles;

    @NotNull(message = "Appointment date is required")
    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private Date appointmentDate;


    @NotBlank(message = "Appraisal location is required")
    private String appraisalLocation;
}

