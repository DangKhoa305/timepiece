package app.timepiece.repository;

import app.timepiece.dto.AppraisalRequestListDTO;
import app.timepiece.entity.AppraisalRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppraisalRequestRepository extends JpaRepository<AppraisalRequest, Long> {
    Page<AppraisalRequest> findAllByStatus(String status, Pageable pageable);
    Page<AppraisalRequest> findAll(Pageable pageable);

}