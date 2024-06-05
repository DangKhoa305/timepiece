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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public Set<Watch> getWatches() {
        return watches;
    }

    public void setWatches(Set<Watch> watches) {
        this.watches = watches;
    }
}