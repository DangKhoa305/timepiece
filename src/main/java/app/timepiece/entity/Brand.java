package app.timepiece.entity;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "Brand")
public class Brand {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long brandId;

    @Column(nullable = false, unique = true)
    private String brandName;

    @Column(nullable = false)
    private String status;
}
