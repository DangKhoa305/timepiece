package app.timepiece.service.serviceImpl;

import app.timepiece.dto.AppraisalRequestListDTO;
import app.timepiece.entity.AppraisalRequest;
import app.timepiece.entity.RequestImage;
import app.timepiece.repository.*;
import app.timepiece.service.AppraisalRequestService;
import app.timepiece.dto.AppraisalRequestDTO;
import app.timepiece.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AppraisalRequestServiceImpl implements AppraisalRequestService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AppraisalRequestRepository appraisalRequestRepository;

    @Autowired
    private RequestImageRepository requestImageRepository;

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy");

    @Override
    public void createAppraisalRequest(AppraisalRequestDTO appraisalRequestDTO) {
        User user = new User();
        user.setName(appraisalRequestDTO.getName());
        user.setPhoneNumber(appraisalRequestDTO.getPhoneNumber());
        User savedUser = userRepository.save(user);

        AppraisalRequest appraisalRequest = new AppraisalRequest();
        appraisalRequest.setUsers(savedUser);
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
        AppraisalRequest savedAppraisalRequest = appraisalRequestRepository.save(appraisalRequest);

        for (String imageUrl : appraisalRequestDTO.getImageUrls()) {
            RequestImage requestImage = new RequestImage();
            requestImage.setAppraisalRequest(savedAppraisalRequest);
            requestImage.setImageUrl(imageUrl);
            requestImageRepository.save(requestImage);
        }
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