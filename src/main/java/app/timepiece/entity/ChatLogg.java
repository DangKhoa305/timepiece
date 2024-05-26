package app.timepiece.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "ChatLogg")
public class ChatLogg {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chatId;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "watchId", nullable = false)
    private Watch watch;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @Column(nullable = false)
    private String message;
}
