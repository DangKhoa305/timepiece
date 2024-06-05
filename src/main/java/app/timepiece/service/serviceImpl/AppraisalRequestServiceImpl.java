package app.timepiece.service.serviceImpl;

import app.timepiece.entity.AppraisalRequest;
import app.timepiece.entity.RequestImage;
import app.timepiece.repository.*;
import app.timepiece.service.AppraisalRequestService;
import app.timepiece.dto.AppraisalRequestDTO;
import app.timepiece.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class AppraisalRequestServiceImpl implements AppraisalRequestService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AppraisalRequestRepository appraisalRequestRepository;

    @Autowired
    private RequestImageRepository requestImageRepository;

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
}

