package app.timepiece.dto;

import lombok.*;

import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppraisalRequestDTO {
    private String name;
    private String email;
    private String phoneNumber;
    private boolean hasOriginalBox;
    private boolean hasPapersOrWarranty;
    private boolean hasPurchaseReceipt;
    private boolean Arethereanystickers;
    private int age;
    private double desiredPrice;
    private String description;
    private String brand;
    private String referenceCode;
    private List<String> imageUrls;


}
