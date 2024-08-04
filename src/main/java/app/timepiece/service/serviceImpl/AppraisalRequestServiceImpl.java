package app.timepiece.service.serviceImpl;

import app.timepiece.dto.AppraisalRequestResponseDTO;
import app.timepiece.entity.Account;
import app.timepiece.dto.AppraisalRequestListDTO;
import app.timepiece.entity.AppraisalRequest;
import app.timepiece.entity.RequestImage;
import app.timepiece.entity.User;
import app.timepiece.repository.*;
import app.timepiece.service.AppraisalRequestService;
import app.timepiece.dto.AppraisalRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AppraisalRequestServiceImpl implements AppraisalRequestService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AppraisalRequestRepository appraisalRequestRepository;

    @Autowired
    private RequestImageRepository requestImageRepository;

    @Autowired
    private CloudinaryService cloudinaryService;

    @Override
    public Boolean createAppraisalRequest(AppraisalRequestDTO appraisalRequestDTO)  {
        Optional<Account> optionalAccount = accountRepository.findByEmail(appraisalRequestDTO.getEmail());

        if (optionalAccount.isEmpty()) {
            // Nếu người dùng không tồn tại
            String errorMessage = "User not found with email: " + appraisalRequestDTO.getEmail();
            throw new Error( errorMessage);
        }
        // Lấy người dùng từ Optional<Account>
        Account account = optionalAccount.get();

        AppraisalRequest appraisalRequest = new AppraisalRequest();
        appraisalRequest.setUsers(account.getUser());
        appraisalRequest.setUsername(appraisalRequestDTO.getName());
        appraisalRequest.setEmail(appraisalRequestDTO.getEmail());
        appraisalRequest.setPhoneNumber(appraisalRequestDTO.getPhoneNumber());
        appraisalRequest.setHasOriginalBox(appraisalRequestDTO.isHasOriginalBox());
        appraisalRequest.setHasPapersOrWarranty(appraisalRequestDTO.isHasPapersOrWarranty());
        appraisalRequest.setHasPurchaseReceipt(appraisalRequestDTO.isHasPurchaseReceipt());
        appraisalRequest.setArethereanystickers(appraisalRequestDTO.isAreThereAnyStickers());
        appraisalRequest.setAge(appraisalRequestDTO.getAge());
        appraisalRequest.setDesiredPrice(appraisalRequestDTO.getDesiredPrice());
        appraisalRequest.setDescription(appraisalRequestDTO.getDescription());
        appraisalRequest.setBrand(appraisalRequestDTO.getBrand());
        appraisalRequest.setReferenceCode(appraisalRequestDTO.getReferenceCode());
        appraisalRequest.setStatus("wait");
        appraisalRequest.setCreateDate(new Date());
        appraisalRequest.setUpdateDate(new Date());
        appraisalRequest.setAppointmentDate(appraisalRequestDTO.getAppointmentDate());
        appraisalRequest.setAppraisalLocation(appraisalRequestDTO.getAppraisalLocation());


        AppraisalRequest savedAppraisalRequest = appraisalRequestRepository.save(appraisalRequest);

        for (MultipartFile imageFile : appraisalRequestDTO.getImageFiles()) {
            try {
                Map uploadResult = cloudinaryService.uploadFile(imageFile);
                String uploadedImageUrl = uploadResult.get("url").toString();
                RequestImage requestImage = new RequestImage();
                requestImage.setAppraisalRequest(savedAppraisalRequest);
                requestImage.setImageUrl(uploadedImageUrl);
                requestImageRepository.save(requestImage);
            } catch (Exception e) {
                throw new Error(e) ;
            }
        }
        return true;
    }

    @Override
    public AppraisalRequestResponseDTO getAppraisalRequestById(Long id) {
        AppraisalRequest appraisalRequest = appraisalRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("AppraisalRequest not found with id: " + id));

        List<RequestImage> images = requestImageRepository.findByAppraisalRequestId(id);

        List<String> imageUrls = images.stream()
                .map(RequestImage::getImageUrl)
                .collect(Collectors.toList());

        // Create and return the DTO
        return AppraisalRequestResponseDTO.builder()
                .id(appraisalRequest.getId())
                .name(appraisalRequest.getUsername())
                .email(appraisalRequest.getEmail())
                .phoneNumber(appraisalRequest.getPhoneNumber())
                .hasOriginalBox(appraisalRequest.isHasOriginalBox())
                .hasPapersOrWarranty(appraisalRequest.isHasPapersOrWarranty())
                .hasPurchaseReceipt(appraisalRequest.isHasPurchaseReceipt())
                .arethereanystickers(appraisalRequest.isArethereanystickers())
                .age(appraisalRequest.getAge())
                .desiredPrice(appraisalRequest.getDesiredPrice())
                .description(appraisalRequest.getDescription())
                .brand(appraisalRequest.getBrand())
                .appointmentDate(appraisalRequest.getAppointmentDate())
                .appraisalLocation(appraisalRequest.getAppraisalLocation())
                .referenceCode(appraisalRequest.getReferenceCode())
                .imageUrls(imageUrls)
                .status(appraisalRequest.getStatus())
                .pdfUrl(appraisalRequest.getPdfUrl())
                .build();
    }

    @Override
    public Page<AppraisalRequestListDTO> getAllAppraisalRequestsByStatusAndAppraiser(String status, Long appraiserId, Pageable pageable) {
        User appraiser = userRepository.findById(appraiserId)
                .orElseThrow(() -> new RuntimeException("Appraiser not found"));

        Page<AppraisalRequest> appraisalRequestsPage;
        if (status == null || status.isEmpty()) {
            appraisalRequestsPage = appraisalRequestRepository.findAllByAppraiserOrderByCreateDateDesc(appraiser, pageable);
        } else {
            appraisalRequestsPage = appraisalRequestRepository.findAllByStatusAndAppraiserOrderByCreateDateDesc(status, appraiser, pageable);
        }
        List<AppraisalRequestListDTO> appraisalRequestsList = appraisalRequestsPage.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return new PageImpl<>(appraisalRequestsList, pageable, appraisalRequestsPage.getTotalElements());
    }

    @Override
    public Page<AppraisalRequestListDTO> getAllAppraisalRequests (Pageable pageable) {
        Page<AppraisalRequest> appraisalRequestsPage = appraisalRequestRepository.findAllByOrderByCreateDateDesc(pageable);
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
                appraisalRequest.getStatus(),
                appraisalRequest.getUpdateDate()
        );
    }

    @Override
    public Boolean updateStatus(Long id, String newStatus) {
        Optional<AppraisalRequest> optionalRequest = appraisalRequestRepository.findById(id);
        if (optionalRequest.isPresent()) {
            AppraisalRequest request = optionalRequest.get();
            request.setStatus(newStatus);
            request.setUpdateDate(new Date());
            appraisalRequestRepository.save(request);
            return true;
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "AppraisalRequest with id " + id + " not found");
        }
    }

    @Override
    @Transactional
    public Boolean updateStatusAndAppraiser(Long id, Long appraiserId) {
        Optional<AppraisalRequest> optionalRequest = appraisalRequestRepository.findById(id);
        if (optionalRequest.isPresent()) {
            AppraisalRequest request = optionalRequest.get();
            request.setStatus("processing");
            request.setUpdateDate(new Date());

            // Assign the appraiser if appraiserId is provided
            if (appraiserId != null) {
                Optional<User> optionalAppraiser = userRepository.findById(appraiserId);
                if (optionalAppraiser.isPresent()) {
                    User appraiser = optionalAppraiser.get();
                    request.setAppraiser(appraiser);
                } else {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User with id " + appraiserId + " not found");
                }
            }
            appraisalRequestRepository.save(request);
            return true;
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "AppraisalRequest with id " + id + " not found");
        }
    }
    @Override
    public Page<AppraisalRequestListDTO> getAllAppraisalRequestsByUser(Long userId, Pageable pageable) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Page<AppraisalRequest> appraisalRequestsPage = appraisalRequestRepository.findAllByUsersOrderByCreateDateDesc(user, pageable);
        List<AppraisalRequestListDTO> appraisalRequestsList = appraisalRequestsPage.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return new PageImpl<>(appraisalRequestsList, pageable, appraisalRequestsPage.getTotalElements());
    }

    @Override
    public AppraisalRequestResponseDTO updatePdfUrlAndStatus(Long appraisalRequestId, String pdfUrl) {
        AppraisalRequest appraisalRequest = appraisalRequestRepository.findById(appraisalRequestId)
                .orElseThrow(() -> new RuntimeException("AppraisalRequest not found"));

        // Cập nhật pdfUrl và status
        appraisalRequest.setPdfUrl(pdfUrl);
        appraisalRequest.setStatus("complete");
        AppraisalRequest updatedAppraisalRequest = appraisalRequestRepository.save(appraisalRequest);

        // Lấy danh sách hình ảnh liên quan
        List<RequestImage> requestImages = requestImageRepository.findByAppraisalRequestId(appraisalRequestId);
        List<String> imageUrls = requestImages.stream()
                .map(RequestImage::getImageUrl)
                .collect(Collectors.toList());

        // Chuyển đổi thành DTO với tất cả các trường
        return AppraisalRequestResponseDTO.builder()
                .id(updatedAppraisalRequest.getId())
                .name(updatedAppraisalRequest.getUsername())
                .email(updatedAppraisalRequest.getEmail())
                .phoneNumber(updatedAppraisalRequest.getPhoneNumber())
                .hasOriginalBox(updatedAppraisalRequest.isHasOriginalBox())
                .hasPapersOrWarranty(updatedAppraisalRequest.isHasPapersOrWarranty())
                .hasPurchaseReceipt(updatedAppraisalRequest.isHasPurchaseReceipt())
                .arethereanystickers(updatedAppraisalRequest.isArethereanystickers())
                .age(updatedAppraisalRequest.getAge())
                .desiredPrice(updatedAppraisalRequest.getDesiredPrice())
                .description(updatedAppraisalRequest.getDescription())
                .brand(updatedAppraisalRequest.getBrand())
                .referenceCode(updatedAppraisalRequest.getReferenceCode())
                .imageUrls(imageUrls)
                .appointmentDate(updatedAppraisalRequest.getAppointmentDate())
                .appraisalLocation(updatedAppraisalRequest.getAppraisalLocation())
                .status(updatedAppraisalRequest.getStatus())
                .pdfUrl(updatedAppraisalRequest.getPdfUrl())
                .build();
    }
}