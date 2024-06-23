package app.timepiece.repository;

import app.timepiece.dto.SearchReportDTO;
import app.timepiece.entity.Report;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {
    @Query("SELECT r FROM Report r WHERE " +
            "(:id IS NULL OR r.id = :id) AND " +
            "(:brand IS NULL OR r.brand LIKE %:brand%) AND " +
            "(:reportStatus IS NULL OR r.reportStatus LIKE %:reportStatus%) AND " +
            "(:createDate IS NULL OR r.createDate = :createDate)")
    Page<Report> searchReport(
            Long id, String brand, String reportStatus, Date createDate, Pageable pageable);

    Page<Report> searchReportByUserId(Long userId, Pageable pageable);
}