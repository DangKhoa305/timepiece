package app.timepiece.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Wallet")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Wallet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Double balance;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;


}
