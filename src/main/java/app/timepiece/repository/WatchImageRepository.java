package app.timepiece.repository;

import app.timepiece.entity.WatchImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WatchImageRepository extends JpaRepository<WatchImage, Long> {
    List<WatchImage> findByWatchId(Long watchId);
}