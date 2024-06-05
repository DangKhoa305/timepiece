package app.timepiece.entity;

import jakarta.persistence.*;
import java.util.Set;

@Entity
@Table(name = "WatchBrand")
public class Brand {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String brandName;

    @Column(nullable = false)
    private String status;

    @OneToMany(mappedBy = "brand")
    private Set<Watch> watches;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Set<Watch> getWatches() {
        return watches;
    }

    public void setWatches(Set<Watch> watches) {
        this.watches = watches;
    }
}
