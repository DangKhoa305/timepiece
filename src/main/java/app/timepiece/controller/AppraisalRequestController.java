package app.timepiece.controller;

import app.timepiece.dto.AppraisalRequestDTO;
import app.timepiece.dto.AppraisalRequestListDTO;
import app.timepiece.service.AppraisalRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/appraisal-requests")
public class AppraisalRequestController {

    @Autowired
    private AppraisalRequestService appraisalRequestService;



    @PostMapping("/createRequest")
    public ResponseEntity<String> createAppraisalRequest(@RequestBody AppraisalRequestDTO appraisalRequestDTO) {
        appraisalRequestService.createAppraisalRequest(appraisalRequestDTO);
        return ResponseEntity.ok("Appraisal request created successfully.");
    }

    @GetMapping("/findByStatus")
    public ResponseEntity<Page<AppraisalRequestListDTO>> getAppraisalRequestsByStatusWait(
            @RequestParam String status,@RequestParam int page, @RequestParam int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<AppraisalRequestListDTO> appraisalRequests = appraisalRequestService.getAllAppraisalRequestsByStatus(status,pageable);
        return ResponseEntity.ok(appraisalRequests);
    }

    @GetMapping("/getAllList")
    public ResponseEntity<Page<AppraisalRequestListDTO>> getAllAppraisalRequests(
            @RequestParam int page, @RequestParam int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<AppraisalRequestListDTO> appraisalRequests = appraisalRequestService.getAllAppraisalRequests(pageable);
        return ResponseEntity.ok(appraisalRequests);
    }
}