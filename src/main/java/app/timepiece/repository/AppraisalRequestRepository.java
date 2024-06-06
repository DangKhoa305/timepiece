package app.timepiece.repository;

import app.timepiece.entity.AppraisalRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppraisalRequestRepository extends JpaRepository<AppraisalRequest, Long> {
}
