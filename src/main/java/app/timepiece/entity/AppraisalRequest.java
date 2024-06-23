package app.timepiece.entity;

import jakarta.persistence.*;
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
    private String referenceCode ;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User users;

    @ManyToOne
    @JoinColumn(name = "appraiser_id")
    private User appraiser;
    // Getters and setters
}