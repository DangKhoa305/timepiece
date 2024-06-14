package app.timepiece.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "WatchBrand")
public class Brand {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String brandName;

    @Column(nullable = false)
    private String status;

    @OneToMany(mappedBy = "brand")
    @ToString.Exclude
    private Set<Watch> watches;

}
