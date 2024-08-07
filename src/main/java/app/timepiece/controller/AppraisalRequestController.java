package app.timepiece.controller;

import app.timepiece.dto.AppraisalRequestDTO;
import app.timepiece.dto.AppraisalRequestListDTO;
import app.timepiece.dto.AppraisalRequestResponseDTO;
import app.timepiece.service.AppraisalRequestService;
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

import java.util.stream.Collectors;

@CrossOrigin
@RestController
@RequestMapping("/api/appraisal-requests")
public class AppraisalRequestController {

    @Autowired
    private AppraisalRequestService appraisalRequestService;

    @PostMapping(value = "/create", consumes = {"multipart/form-data"})
    public ResponseEntity<?> createAppraisalRequest( @Valid @ModelAttribute AppraisalRequestDTO appraisalRequestDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errors = bindingResult.getFieldErrors().stream()
                    .map(error -> error.getField() + ": " + error.getDefaultMessage())
                    .collect(Collectors.joining("\n"));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Validation errors:\n" + errors);
        }
        try {
            appraisalRequestService.createAppraisalRequest(appraisalRequestDTO);
            return ResponseEntity.status(HttpStatus.OK).body("Create Appraisal Request successfully ");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppraisalRequestResponseDTO> getAppraisalRequestById(@PathVariable Long id) {
        AppraisalRequestResponseDTO appraisalRequestDTO = appraisalRequestService.getAppraisalRequestById(id);
        return ResponseEntity.ok(appraisalRequestDTO);
    }

    @PreAuthorize("hasRole('Appraiser')or hasRole('Admin')")
    @GetMapping("/findByStatusAndAppraiser")
    public ResponseEntity<Page<AppraisalRequestListDTO>> getAppraisalRequestsByStatusAndAppraiser(
            @RequestParam(required = false) String status, @RequestParam Long appraiserId, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<AppraisalRequestListDTO> appraisalRequests = appraisalRequestService.getAllAppraisalRequestsByStatusAndAppraiser(status, appraiserId, pageable);
        return ResponseEntity.ok(appraisalRequests);
    }

    @PreAuthorize("hasRole('Appraiser')or hasRole('Admin')")
    @GetMapping("/getAllList")
    public ResponseEntity<Page<AppraisalRequestListDTO>> getAllAppraisalRequests(
            @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<AppraisalRequestListDTO> appraisalRequests = appraisalRequestService.getAllAppraisalRequests(pageable);
        return ResponseEntity.ok(appraisalRequests);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<String> updateStatus(@PathVariable Long id, @RequestParam String newStatus) {
        appraisalRequestService.updateStatus(id, newStatus);
        return ResponseEntity.ok("Status updated successfully");
    }

    @PutMapping("/{id}/accept-requests")
    public ResponseEntity<String> updateStatusAndAppraiser(
            @PathVariable Long id,
            @RequestParam(required = false) Long appraiserId) {

        Boolean updated = appraisalRequestService.updateStatusAndAppraiser(id, appraiserId);
        if (updated) {
            return ResponseEntity.ok("AppraisalRequest updated successfully");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/findByUser")
    public ResponseEntity<Page<AppraisalRequestListDTO>> getAppraisalRequestsByUser(
            @RequestParam Long userId, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<AppraisalRequestListDTO> appraisalRequests = appraisalRequestService.getAllAppraisalRequestsByUser(userId, pageable);
        return ResponseEntity.ok(appraisalRequests);
    }

    @PreAuthorize("hasRole('Appraiser') or hasRole('Admin')")
    @PostMapping(value = "/complete-request/{id}")
    public ResponseEntity<AppraisalRequestResponseDTO> updatePdfUrl(@PathVariable Long id, @RequestParam String pdfUrl) {
        try {
            AppraisalRequestResponseDTO responseDTO = appraisalRequestService.updatePdfUrlAndStatus(id, pdfUrl);
            return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}