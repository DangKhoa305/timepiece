package app.timepiece.service;

import app.timepiece.dto.AppraisalRequestDTO;
import app.timepiece.dto.AppraisalRequestListDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AppraisalRequestService {
    void createAppraisalRequest(AppraisalRequestDTO appraisalRequestDTO);

    Page<AppraisalRequestListDTO> getAllAppraisalRequestsByStatus(String status, Pageable pageable);
    Page<AppraisalRequestListDTO> getAllAppraisalRequests(Pageable pageable);
}
