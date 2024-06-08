package app.timepiece.service;

import app.timepiece.dto.AppraisalRequestDTO;
import app.timepiece.dto.AppraisalRequestListDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AppraisalRequestService {
    void createAppraisalRequest(AppraisalRequestDTO appraisalRequestDTO);
    Page<AppraisalRequestListDTO> getAllAppraisalRequestsByStatusWait(Pageable pageable);
    Page<AppraisalRequestListDTO> getAllAppraisalRequestsByStatusProcessing(Pageable pageable);
    Page<AppraisalRequestListDTO> getAllAppraisalRequestsByStatusComplete(Pageable pageable);
    Page<AppraisalRequestListDTO> getAllAppraisalRequests(Pageable pageable);
}
