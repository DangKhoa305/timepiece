package app.timepiece.entity;

import jakarta.persistence.*;

@Entity
public class AppraisalRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private boolean hasOriginalBox;
    private boolean hasPapersOrWarranty;
    private boolean hasPurchaseReceipt;
    private double desiredPrice;

    @Column(length = 1000)
    private String imagePaths;

    @ManyToOne
    @JoinColumn(name = "watch_id")
    private Watch watch;

    // Getters and setters
}
