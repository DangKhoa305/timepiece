package app.timepiece.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "WatchImage")
public class WatchImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "watch_id", nullable = false)
    @ToString.Exclude
    private Watch watch;

    @Column(nullable = false)
    private String imageUrl;


}
