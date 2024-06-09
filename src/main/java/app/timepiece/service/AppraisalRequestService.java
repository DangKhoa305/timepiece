package app.timepiece.service;

import app.timepiece.dto.AppraisalRequestDTO;
import app.timepiece.dto.AppraisalRequestResponseDTO;
import app.timepiece.dto.AppraiserRequestStatusDTO;
import org.springframework.http.ResponseEntity;
import app.timepiece.dto.AppraisalRequestListDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface AppraisalRequestService {
    Boolean createAppraisalRequest(AppraisalRequestDTO appraisalRequestDTO);
    AppraisalRequestResponseDTO getAppraisalRequestById(Long id);
    Page<AppraisalRequestListDTO> getAllAppraisalRequestsByStatus(String status, Pageable pageable);
    Page<AppraisalRequestListDTO> getAllAppraisalRequests(Pageable pageable);
    AppraiserRequestStatusDTO updateStatus(Long id, String newStatus);
}
