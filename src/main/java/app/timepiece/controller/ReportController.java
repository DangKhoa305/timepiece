package app.timepiece.controller;

import app.timepiece.dto.ReportDTO;
import app.timepiece.dto.ReportResponseDTO;
import app.timepiece.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/appraisal-report")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @PreAuthorize("hasRole('Appraiser')or hasRole('Admin')")
    @PostMapping(value = "/create", consumes = {"multipart/form-data"})
    public ResponseEntity<String> createReport(@ModelAttribute ReportDTO reportDTO) {
        try {
            reportService.createReport(reportDTO);
            return ResponseEntity.status(HttpStatus.OK).body("Create Appraisal Report successfully ");
        } catch (Error e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReportResponseDTO> getReportById(@PathVariable Long id) {
        ReportResponseDTO reportResponseDTO = reportService.getReportById(id);
        return ResponseEntity.ok(reportResponseDTO);
    }
}
