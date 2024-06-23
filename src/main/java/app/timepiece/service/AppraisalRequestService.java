package app.timepiece.service;

import app.timepiece.dto.AppraisalRequestDTO;
import app.timepiece.dto.AppraisalRequestResponseDTO;
import app.timepiece.dto.AppraisalRequestListDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;


public interface AppraisalRequestService {
    Boolean createAppraisalRequest(AppraisalRequestDTO appraisalRequestDTO);
    AppraisalRequestResponseDTO getAppraisalRequestById(Long id);
    Page<AppraisalRequestListDTO> getAllAppraisalRequests(Pageable pageable);
    Boolean updateStatus(Long id, String newStatus);
    Page<AppraisalRequestListDTO> getAllAppraisalRequestsByStatusAndAppraiser(String status, Long appraiserId, Pageable pageable);
    Page<AppraisalRequestListDTO> getAllAppraisalRequestsByUser(Long userId, Pageable pageable);
    @Transactional
    Boolean updateStatusAndAppraiser(Long id, String newStatus, Long appraiserId);
}
