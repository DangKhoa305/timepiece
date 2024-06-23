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
@Table(name = "Report")
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String brand;

    @Column(nullable = false)
    private String model;

    @Column(nullable = false)
    private String referenceCode ;

    @Column(nullable = false)
    private String watchType;

    @Column(nullable = false)
    private String material;

    @Column(nullable = false)
    private String watchStrap;

    @Column(nullable = false)
    private int yearProduced;

    @Column(nullable = false)
    private String watchStatus;

    @Column(nullable = true)
    private String accessories;

    @Column(nullable = true)
    private String origin;

    @Column(nullable = false)
    private String size;

    @Column(nullable = false)
    private Date createDate;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String commentValue;

    @Column(nullable = false)
    private String reportStatus;
}
