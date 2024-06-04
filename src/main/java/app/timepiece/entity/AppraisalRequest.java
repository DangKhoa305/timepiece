package app.timepiece.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "AppraisalRequest")
public class AppraisalRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private boolean hasOriginalBox;
    private boolean hasPapersOrWarranty;
    private boolean hasPurchaseReceipt;

    @Column(nullable = false)
    private double desiredPrice;

    private String description;

    @Column(nullable = false)
    private String brand;

    @Column(nullable = false)
    private String referenceCode ;



    // Getters and setters
}
