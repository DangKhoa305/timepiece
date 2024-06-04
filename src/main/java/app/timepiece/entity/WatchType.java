package app.timepiece.entity;

import jakarta.persistence.*;
import java.util.Set;

@Entity
@Table(name = "WatchType")
public class WatchType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String typeName;

    @OneToMany(mappedBy = "watchType")
    private Set<Watch> watches;

    // Getters and setters
}