package app.timepiece.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

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
    @ToString.Exclude
    private User user;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String watchStatus;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private double price;

    @ManyToOne
    @JoinColumn(name = "brand_id", nullable = false)
    @ToString.Exclude
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

    @Column(nullable = false)
    private String placeOfProduction;

    @Column(nullable = false)
    private String address;

    private Date createDate;

    private Date updateDate;

    private Date vipEndDate;

    @ManyToOne
    @JoinColumn(name = "watch_type_id", nullable = false)
    @ToString.Exclude
    private WatchType watchType;
}