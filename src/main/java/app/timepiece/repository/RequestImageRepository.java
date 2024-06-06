package app.timepiece.repository;

import app.timepiece.entity.RequestImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RequestImageRepository extends JpaRepository<RequestImage, Long> {
}