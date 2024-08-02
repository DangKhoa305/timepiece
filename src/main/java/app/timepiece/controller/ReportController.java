package app.timepiece.controller;

import app.timepiece.dto.ReportDTO;
import app.timepiece.dto.ReportResponseDTO;
import app.timepiece.dto.SearchReportDTO;
import app.timepiece.entity.Report;
import app.timepiece.service.ReportService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@RequestMapping("/api/appraisal-report")
public class ReportController {

    @Autowired
    private ReportService reportService;

//    @PreAuthorize("hasRole('Appraiser')or hasRole('Admin')")
//    @PostMapping(value = "/create", consumes = {"multipart/form-data"})
//    public ResponseEntity<String> createReport(@Valid @ModelAttribute ReportDTO reportDTO, BindingResult bindingResult) {
//        if (bindingResult.hasErrors()) {
//            String errors = bindingResult.getFieldErrors().stream()
//                    .map(error -> error.getField() + ": " + error.getDefaultMessage())
//                    .collect(Collectors.joining("\n"));
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Validation errors:\n" + errors);
//        }
//        try {
//            reportService.createReport(reportDTO);
//            return ResponseEntity.status(HttpStatus.OK).body("Create Appraisal Report successfully ");
//        } catch (Error e) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
//        }
//    }

    @PreAuthorize("hasRole('Appraiser') or hasRole('Admin')")
    @PostMapping(value = "/create", consumes = {"multipart/form-data"})
    public ResponseEntity<String> createReport(@Valid @ModelAttribute ReportDTO reportDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errors = bindingResult.getFieldErrors().stream()
                    .map(error -> error.getField() + ": " + error.getDefaultMessage())
                    .collect(Collectors.joining("\n"));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Validation errors:\n" + errors);
        }
        try {
            Report report = reportService.createReport(reportDTO);
            String pdfUrl = report.getPdfUrl();
            return ResponseEntity.status(HttpStatus.OK).body("Create Appraisal Report successfully. Download PDF from: " + pdfUrl);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReportResponseDTO> getReportById(@PathVariable Long id) {
        ReportResponseDTO reportResponseDTO = reportService.getReportById(id);
        return ResponseEntity.ok(reportResponseDTO);
    }

    @GetMapping("/search")
    public Page<SearchReportDTO> searchReports(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false, defaultValue = "") String brand,
            @RequestParam(required = false, defaultValue = "") String reportStatus,
            @RequestParam(required = false) Date createDate,
            @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return reportService.searchReports(id, brand, reportStatus, createDate, pageable);
    }

    @GetMapping("/search/{userId}")
    public Page<SearchReportDTO> getReportsByUserId(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return reportService.searchReportsByUserId(userId, pageable);
    }

}
