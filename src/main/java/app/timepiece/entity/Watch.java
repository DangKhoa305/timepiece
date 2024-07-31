package app.timepiece.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

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

    private String area;

    private String appraisalCertificateUrl;

    @Column(name = "has_appraisal_certificate", nullable = false)
    private boolean hasAppraisalCertificate;

    @ManyToOne
    @JoinColumn(name = "watch_type_id", nullable = false)
    @ToString.Exclude
    private WatchType watchType;

    @OneToMany(mappedBy = "watch", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<WatchImage> images;


    private Date endDate;
    private  Date startDate;
    private int numberDatePost;
    private boolean expired;

    @ManyToOne
    @JoinColumn(name = "renewal_package_id")
    @ToString.Exclude
    private RenewalPackage renewalPackage;


}