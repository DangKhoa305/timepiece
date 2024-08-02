package app.timepiece.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppraisalRequestResponseDTO {
        private Long id;
        private String name;
        private String email;
        private String phoneNumber;
        private boolean hasOriginalBox;
        private boolean hasPapersOrWarranty;
        private boolean hasPurchaseReceipt;
        private boolean arethereanystickers;
        private int age;
        private double desiredPrice;
        private String description;
        private String brand;
        private String referenceCode;
        private List<String> imageUrls;
        private Date appointmentDate;
        private String appointmentTime;
        private String appraisalLocation;
        private String status;
        private String pdfUrl;
    }
