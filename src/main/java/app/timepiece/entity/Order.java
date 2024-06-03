package app.timepiece.entity;

import jakarta.persistence.*;
import java.util.Set;

@Entity
@Table(name = "watchOrder")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String note;
    private String orderDate;
    private double totalPrice;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "order")
    private Set<Feedback> feedbacks;

    // Getters and setters
}