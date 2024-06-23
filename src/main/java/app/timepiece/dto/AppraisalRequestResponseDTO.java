package app.timepiece.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppraisalRequestResponseDTO {
        private String name;
        private String email;
        private String phoneNumber;
        private String address;
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


    }
