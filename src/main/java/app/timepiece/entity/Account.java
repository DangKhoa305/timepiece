package app.timepiece.entity;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String password;

    @OneToOne(mappedBy = "account")
    private User user;

    // Getters and setters
}
