package app.timepiece.repository;
import app.timepiece.entity.Watch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WatchRepository extends JpaRepository<Watch, Long> {
    List<Watch> findByNameContainingIgnoreCase(String name);
    List<Watch> findByUserIdAndStatus(Long userId, String status);
}