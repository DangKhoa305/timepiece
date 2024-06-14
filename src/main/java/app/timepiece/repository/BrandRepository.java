package app.timepiece.repository;

import app.timepiece.entity.AppraisalRequest;
import app.timepiece.entity.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Long> {

}
