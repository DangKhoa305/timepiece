package app.timepiece.repository;

import app.timepiece.entity.AppraisalRequest;
import app.timepiece.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface AppraisalRequestRepository extends JpaRepository<AppraisalRequest, Long> {
    Page<AppraisalRequest> findAll(Pageable pageable);
    Page<AppraisalRequest> findAllByStatusAndAppraiser(String status, User appraiser, Pageable pageable);
    Page<AppraisalRequest> findAllByAppraiser(User appraiser, Pageable pageable);
    Page<AppraisalRequest> findAllByUsers(User user, Pageable pageable);
}
