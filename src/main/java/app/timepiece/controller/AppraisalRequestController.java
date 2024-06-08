package app.timepiece.controller;

import app.timepiece.dto.AppraisalRequestDTO;
import app.timepiece.dto.AppraisalRequestListDTO;
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


    @PostMapping("/create")
    public ResponseEntity<String> createAppraisalRequest(@RequestBody AppraisalRequestDTO appraisalRequestDTO) {
        ResponseEntity<String> responseEntity = appraisalRequestService.createAppraisalRequest(appraisalRequestDTO);
        if (responseEntity.getStatusCode() == HttpStatus.NOT_FOUND) {
            // Xử lý trường hợp người dùng không tồn tại
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseEntity.getBody());
        } else {
            // Xử lý trường hợp tạo yêu cầu đánh giá thành công
            return ResponseEntity.status(HttpStatus.OK).body(responseEntity.getBody());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppraisalRequestDTO> getAppraisalRequestById(@PathVariable Long id) {
        AppraisalRequestDTO appraisalRequestDTO = appraisalRequestService.getAppraisalRequestById(id);
        return ResponseEntity.ok(appraisalRequestDTO);
    }

    @PreAuthorize("hasRole('Appraiser')or hasRole('Admin')")
    @GetMapping("/findByStatus")
    public ResponseEntity<Page<AppraisalRequestListDTO>> getAppraisalRequestsByStatus(
            @RequestParam String status,@RequestParam int page, @RequestParam int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<AppraisalRequestListDTO> appraisalRequests = appraisalRequestService.getAllAppraisalRequestsByStatus(status,pageable);
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
}