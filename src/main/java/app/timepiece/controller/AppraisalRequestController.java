package app.timepiece.controller;
import app.timepiece.dto.*;

import app.timepiece.service.AppraisalRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/appraisal")
public class AppraisalRequestController {

    @Autowired
    private AppraisalRequestService appraisalRequestservice;

    @PostMapping("/create")
    public ResponseEntity<String> createAppraisalRequest(@RequestBody AppraisalRequestDTO appraisalRequestDTO) {
        ResponseEntity<String> responseEntity = appraisalRequestservice.createAppraisalRequest(appraisalRequestDTO);
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
        AppraisalRequestDTO appraisalRequestDTO = appraisalRequestservice.getAppraisalRequestById(id);
        return ResponseEntity.ok(appraisalRequestDTO);
    }
}

