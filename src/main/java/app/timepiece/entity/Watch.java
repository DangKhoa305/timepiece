package app.timepiece.entity;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "Watch")
public class Watch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long watchId;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
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
    @JoinColumn(name = "brandId", nullable = false)
    private Brand brand;

    @Column(nullable = false)
    private int yearProduced;

    @Column(nullable = false)
    private String watchType;
}
