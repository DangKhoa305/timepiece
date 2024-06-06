package app.timepiece.controller;
import app.timepiece.dto.*;

import app.timepiece.service.AppraisalRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/appraisal")
public class AppraisalRequestController {

    @Autowired
    private AppraisalRequestService appraisalRequestservice;

    @PostMapping("/create")
    public ResponseEntity<String> createAppraisalRequest(@RequestBody AppraisalRequestDTO appraisalRequestDTO) {
        appraisalRequestservice.createAppraisalRequest(appraisalRequestDTO);
        return ResponseEntity.ok("Appraisal request created successfully.");
    }
}

