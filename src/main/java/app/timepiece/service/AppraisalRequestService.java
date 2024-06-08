package app.timepiece.service;

import app.timepiece.dto.AppraisalRequestDTO;
import org.springframework.http.ResponseEntity;
import app.timepiece.dto.AppraisalRequestListDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface AppraisalRequestService {
    ResponseEntity<String> createAppraisalRequest(AppraisalRequestDTO appraisalRequestDTO);
    AppraisalRequestDTO getAppraisalRequestById(Long id);
    Page<AppraisalRequestListDTO> getAllAppraisalRequestsByStatus(String status, Pageable pageable);
    Page<AppraisalRequestListDTO> getAllAppraisalRequests(Pageable pageable);
}
