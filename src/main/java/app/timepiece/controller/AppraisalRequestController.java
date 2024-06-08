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

    private final AppraisalRequestService appraisalRequestService;

    @Autowired
    public AppraisalRequestController(AppraisalRequestService appraisalRequestService) {
        this.appraisalRequestService = appraisalRequestService;
    }

    @PostMapping("/createRequest")
    public ResponseEntity<String> createAppraisalRequest(@RequestBody AppraisalRequestDTO appraisalRequestDTO) {
        appraisalRequestService.createAppraisalRequest(appraisalRequestDTO);
        return ResponseEntity.ok("Appraisal request created successfully.");
    }

    @GetMapping("/getListWait")
    public ResponseEntity<Page<AppraisalRequestListDTO>> getAppraisalRequestsByStatusWait(
            @RequestParam int page, @RequestParam int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<AppraisalRequestListDTO> appraisalRequests = appraisalRequestService.getAllAppraisalRequestsByStatusWait(pageable);
        return ResponseEntity.ok(appraisalRequests);
    }

    @GetMapping("/getListProcessing")
    public ResponseEntity<Page<AppraisalRequestListDTO>> getAppraisalRequestsByStatusProcessing(
            @RequestParam int page, @RequestParam int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<AppraisalRequestListDTO> appraisalRequests = appraisalRequestService.getAllAppraisalRequestsByStatusProcessing(pageable);
        return ResponseEntity.ok(appraisalRequests);
    }

    @GetMapping("/getListComplete")
    public ResponseEntity<Page<AppraisalRequestListDTO>> getAppraisalRequestsByStatusComplete(
            @RequestParam int page, @RequestParam int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<AppraisalRequestListDTO> appraisalRequests = appraisalRequestService.getAllAppraisalRequestsByStatusComplete(pageable);
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