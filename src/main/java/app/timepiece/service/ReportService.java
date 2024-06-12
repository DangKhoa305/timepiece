package app.timepiece.service;

import app.timepiece.dto.ReportDTO;
import app.timepiece.dto.ReportResponseDTO;
import app.timepiece.dto.SearchReportDTO;
import app.timepiece.entity.Report;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;

public interface ReportService {
    Report createReport(ReportDTO reportDTO);
    ReportResponseDTO getReportById(Long id);
    Page<SearchReportDTO> searchReports(Long id, String brand, String reportStatus, Date createDate, Pageable pageable);
}
