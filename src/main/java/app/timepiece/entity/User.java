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
@Table(name = "User")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String address;
    private String avatar;
    private String phoneNumber;
    private String status;
    private Date dateCreate;
    private String gender;
    private String birthday;
    private String citizenID;

    @OneToOne
    @JoinColumn(name = "account_id")
    private Account account;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    @OneToMany(mappedBy = "user")
    private Set<Order> orders;

    @OneToMany(mappedBy = "user")
    private Set<ChatLog> chatLogs;

    @OneToMany(mappedBy = "user")
    private Set<Report> reports;

    // Getters and setters
}
