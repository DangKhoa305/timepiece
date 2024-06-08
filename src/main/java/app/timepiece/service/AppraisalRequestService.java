package app.timepiece.service;

import app.timepiece.dto.AppraisalRequestDTO;
import org.springframework.http.ResponseEntity;

public interface AppraisalRequestService {
    ResponseEntity<String> createAppraisalRequest(AppraisalRequestDTO appraisalRequestDTO);
    AppraisalRequestDTO getAppraisalRequestById(Long id);
}
