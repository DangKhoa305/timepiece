package app.timepiece.entity;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "Report")
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reportId;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "watchId", nullable = false)
    private Watch watch;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private double minValue;

    @Column(nullable = false)
    private double maxValue;

    @Column(nullable = false)
    private String authenticityVerification;

    @Column(nullable = false)
    private String estimation;

    @Column(nullable = false)
    private String reportStatus;
}
