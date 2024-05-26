package app.timepiece.entity;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "User")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @ManyToOne
    @JoinColumn(name = "account", nullable = false)
    private Account account;

    private String name;
    private String address;
    private String avatar;
    private String phoneNumber;

    @ManyToOne
    @JoinColumn(name = "roleId", nullable = false)
    private Role role;

    private String status;


}
