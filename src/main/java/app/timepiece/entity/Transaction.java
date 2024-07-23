package app.timepiece.entity;


import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "Transaction")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Double amount;
    private Date createdAt;
    private String transactionType;
    private String description;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    private User user;

    @ManyToOne
    @JoinColumn(name = "orderId")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "walletId")
    private Wallet wallet;

    @ManyToOne
    @JoinColumn(name = "watchId")
    private Watch watch;


}

