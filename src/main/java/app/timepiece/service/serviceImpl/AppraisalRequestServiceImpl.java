package app.timepiece.service.serviceImpl;

import app.timepiece.entity.Account;
import app.timepiece.dto.AppraisalRequestListDTO;
import app.timepiece.entity.AppraisalRequest;
import app.timepiece.entity.RequestImage;
import app.timepiece.repository.*;
import app.timepiece.service.AppraisalRequestService;
import app.timepiece.dto.AppraisalRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AppraisalRequestServiceImpl implements AppraisalRequestService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AppraisalRequestRepository appraisalRequestRepository;

    @Autowired
    private RequestImageRepository requestImageRepository;

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy");

    @Override
    public ResponseEntity<String> createAppraisalRequest(AppraisalRequestDTO appraisalRequestDTO) { // Kiểm tra xem người dùng đã tồn tại không
        Optional<Account> optionalAccount = accountRepository.findByEmail(appraisalRequestDTO.getEmail());

        if (optionalAccount.isEmpty()) {
            // Nếu người dùng không tồn tại
            String errorMessage = "User not found with email: " + appraisalRequestDTO.getEmail();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
        }
        // Lấy người dùng từ Optional<Account>
        Account account = optionalAccount.get();

        AppraisalRequest appraisalRequest = new AppraisalRequest();
        appraisalRequest.setUsers(account.getUser());
        appraisalRequest.setHasOriginalBox(appraisalRequestDTO.isHasOriginalBox());
        appraisalRequest.setHasPapersOrWarranty(appraisalRequestDTO.isHasPapersOrWarranty());
        appraisalRequest.setHasPurchaseReceipt(appraisalRequestDTO.isHasPurchaseReceipt());
        appraisalRequest.setArethereanystickers(appraisalRequestDTO.isArethereanystickers());
        appraisalRequest.setAge(appraisalRequestDTO.getAge());
        appraisalRequest.setDesiredPrice(appraisalRequestDTO.getDesiredPrice());
        appraisalRequest.setDescription(appraisalRequestDTO.getDescription());
        appraisalRequest.setBrand(appraisalRequestDTO.getBrand());
        appraisalRequest.setReferenceCode(appraisalRequestDTO.getReferenceCode());
        appraisalRequest.setStatus("wait");
        appraisalRequest.setCreateDate(new Date());
        appraisalRequest.setUpdateDate(new Date());
        AppraisalRequest savedAppraisalRequest = appraisalRequestRepository.save(appraisalRequest);

        for (String imageUrl : appraisalRequestDTO.getImageUrls()) {
            RequestImage requestImage = new RequestImage();
            requestImage.setAppraisalRequest(savedAppraisalRequest);
            requestImage.setImageUrl(imageUrl);
            requestImageRepository.save(requestImage);
        }
        return ResponseEntity.status(HttpStatus.OK).body("Appraisal request created successfully.");
    }

    @Override
    public AppraisalRequestDTO getAppraisalRequestById(Long id) {
        AppraisalRequest appraisalRequest = appraisalRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("AppraisalRequest not found with id: " + id));

        List<RequestImage> images = requestImageRepository.findByAppraisalRequestId(id);

        List<String> imageUrls = images.stream()
                .map(RequestImage::getImageUrl)
                .collect(Collectors.toList());

        // Create and return the DTO
        return AppraisalRequestDTO.builder()
                .name(appraisalRequest.getUsers().getName())
                .email(appraisalRequest.getUsers().getAccount().getEmail())
                .phoneNumber(appraisalRequest.getUsers().getPhoneNumber())
                .hasOriginalBox(appraisalRequest.isHasOriginalBox())
                .hasPapersOrWarranty(appraisalRequest.isHasPapersOrWarranty())
                .hasPurchaseReceipt(appraisalRequest.isHasPurchaseReceipt())
                .Arethereanystickers(appraisalRequest.isArethereanystickers())
                .age(appraisalRequest.getAge())
                .desiredPrice(appraisalRequest.getDesiredPrice())
                .description(appraisalRequest.getDescription())
                .brand(appraisalRequest.getBrand())
                .referenceCode(appraisalRequest.getReferenceCode())
                .imageUrls(imageUrls)
                .build();
    }

    @Override
    public Page<AppraisalRequestListDTO> getAllAppraisalRequestsByStatus(String status, Pageable pageable) {
        Page<AppraisalRequest> appraisalRequestsPage = appraisalRequestRepository.findAllByStatus(status,pageable);
        List<AppraisalRequestListDTO> appraisalRequestsList = appraisalRequestsPage.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return new PageImpl<>(appraisalRequestsList, pageable, appraisalRequestsPage.getTotalElements());
    }


    @Override
    public Page<AppraisalRequestListDTO> getAllAppraisalRequests (Pageable pageable) {
        Page<AppraisalRequest> appraisalRequestsPage = appraisalRequestRepository.findAll(pageable);
        List<AppraisalRequestListDTO> appraisalRequestsList = appraisalRequestsPage.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return new PageImpl<>(appraisalRequestsList, pageable, appraisalRequestsPage.getTotalElements());
    }


    private AppraisalRequestListDTO convertToDTO(AppraisalRequest appraisalRequest) {
        return new AppraisalRequestListDTO(
                appraisalRequest.getId(),
                appraisalRequest.getCreateDate(),
                appraisalRequest.getBrand(),
                appraisalRequest.getUpdateDate()
        );
    }
}