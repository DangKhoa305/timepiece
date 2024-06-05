package app.timepiece.entity;

import app.timepiece.dto.WatchDTO;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Watch")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Watch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private double price;

    @ManyToOne
    @JoinColumn(name = "brand_id", nullable = false)
    private Brand brand;

    @Column(nullable = false)
    private int yearProduced;

    @Column(nullable = false)
    private String model;

    @Column(nullable = false)
    private String material;

    @Column(nullable = false)
    private String watchStrap;

    @Column(nullable = false)
    private String size;

    @Column(nullable = true)
    private String accessories;

    @Column(nullable = false)
    private String referenceCode;

    @ManyToOne
    @JoinColumn(name = "watch_type_id", nullable = false)
    private WatchType watchType;
}