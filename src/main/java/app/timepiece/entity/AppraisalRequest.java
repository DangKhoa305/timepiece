package app.timepiece.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "AppraisalRequest")
public class AppraisalRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String email;
    private String phoneNumber;
    private boolean hasOriginalBox;
    private boolean hasPapersOrWarranty;
    private boolean hasPurchaseReceipt;
    private boolean Arethereanystickers;
    private int age;
    private String status;
    private Date createDate;
    private Date updateDate;

    @Column(nullable = false)
    private double desiredPrice;

    private String description;

    @Column(nullable = false)
    private String brand;

    @Column(nullable = false)
    private String referenceCode;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User users;

    @ManyToOne
    @JoinColumn(name = "appraiser_id")
    private User appraiser;

//    @OneToOne(mappedBy = "appraisalRequest")
//    private Report report;
    private String pdfUrl;

    @NotNull
    private Date appointmentDate;

    @NotNull
    private String appointmentTime;

    @NotBlank
    private String appraisalLocation;
}