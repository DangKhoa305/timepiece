package app.timepiece.repository;

import app.timepiece.entity.Watch;
import app.timepiece.entity.WatchType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WatchTypeRepository extends JpaRepository<WatchType, Long> {
}
