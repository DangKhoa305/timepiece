package app.timepiece.service;

import app.timepiece.dto.ReportDTO;
import app.timepiece.dto.ReportResponseDTO;
import app.timepiece.entity.Report;

public interface ReportService {
    Report createReport(ReportDTO reportDTO);
    ReportResponseDTO getReportById(Long id);
}
