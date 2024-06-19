package app.timepiece.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "watchOrder")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String note;
    private Date orderDate;
    private double totalPrice;
    private Date createDate;
    private Date updateDate;
    private String status;

    @ManyToOne
    @ToString.Exclude
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "order")
    @ToString.Exclude
    private Set<Feedback> feedbacks;

    @ManyToOne
    @JoinColumn(name = "watch_id", nullable = false)
    @ToString.Exclude
    private Watch watch;
}