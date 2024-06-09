package app.timepiece.controller;

import app.timepiece.dto.AppraisalRequestDTO;
import app.timepiece.dto.AppraisalRequestListDTO;
import app.timepiece.dto.AppraisalRequestResponseDTO;
import app.timepiece.service.AppraisalRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/appraisal-requests")
public class AppraisalRequestController {

    @Autowired
    private AppraisalRequestService appraisalRequestService;

    @PreAuthorize("hasRole('Appraiser')or hasRole('Admin')")
    @PostMapping(value = "/create", consumes = {"multipart/form-data"})
    public ResponseEntity<String> createAppraisalRequest(@ModelAttribute AppraisalRequestDTO appraisalRequestDTO) {
        try {
             appraisalRequestService.createAppraisalRequest(appraisalRequestDTO);
            return ResponseEntity.status(HttpStatus.OK).body("Create Appraisal Request successfully ");
        } catch (Error e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppraisalRequestResponseDTO> getAppraisalRequestById(@PathVariable Long id) {
        AppraisalRequestResponseDTO appraisalRequestDTO = appraisalRequestService.getAppraisalRequestById(id);
        return ResponseEntity.ok(appraisalRequestDTO);
    }

    @PreAuthorize("hasRole('Appraiser')or hasRole('Admin')")
    @GetMapping("/findByStatus")
    public ResponseEntity<Page<AppraisalRequestListDTO>> getAppraisalRequestsByStatus(
            @RequestParam String status, @RequestParam int page, @RequestParam int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<AppraisalRequestListDTO> appraisalRequests = appraisalRequestService.getAllAppraisalRequestsByStatus(status, pageable);
        return ResponseEntity.ok(appraisalRequests);
    }

    @PreAuthorize("hasRole('Appraiser')or hasRole('Admin')")
    @GetMapping("/getAllList")
    public ResponseEntity<Page<AppraisalRequestListDTO>> getAllAppraisalRequests(
            @RequestParam int page, @RequestParam int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<AppraisalRequestListDTO> appraisalRequests = appraisalRequestService.getAllAppraisalRequests(pageable);
        return ResponseEntity.ok(appraisalRequests);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<String> updateStatus(@PathVariable Long id, @RequestBody String newStatus) {
        appraisalRequestService.updateStatus(id, newStatus);
        return ResponseEntity.ok("Status updated successfully");

    }
}