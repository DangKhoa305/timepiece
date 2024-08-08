package app.timepiece.service.serviceImpl;

import app.timepiece.dto.*;
import app.timepiece.entity.*;
import app.timepiece.repository.*;
import app.timepiece.service.FeedbackService;
import app.timepiece.service.WalletService;
import app.timepiece.service.WatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class WatchServiceImpl implements WatchService {

    @Autowired
    private WatchRepository watchRepository;

    @Autowired
    private WatchImageRepository watchImageRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private WatchTypeRepository watchTypeRepository;

    @Autowired
    private CloudinaryService cloudinaryService;

    @Autowired
    private WalletService walletService;

    @Autowired
    private RenewalPackageRepostory renewalPackageRepository;

    @Autowired
    private FeedbackService feedbackService;


    @Override
    public List<WatchDTO> getAllWatches() {
        List<String> excludedStatuses = Arrays.asList("CANCEL", "SOLD");
        return watchRepository.findAllExcludingStatuses(excludedStatuses).stream()
                .map(this::convertToWatchDTO)
                .collect(Collectors.toList());
    }


    public List<ShowWatchDTO> getTop12WatchesByStatus() {
        List<Watch> watches = watchRepository.findByStatusOrderByCreateDateDesc("SHOW").stream().limit(12).collect(Collectors.toList());
        return watches.stream().map(this::convertToShowWatchDTO).collect(Collectors.toList());
    }

    @Override
    public List<ShowWatchDTO> searchWatchesByName(String name) {
        List<Watch> watches = watchRepository.findByNameContainingIgnoreCase(name);
        return watches.stream().map(this::convertToShowWatchDTO).collect(Collectors.toList());
    }

    private WatchDTO convertToWatchDTO(Watch watch) {
        List<WatchImage> watchImages = watchImageRepository.findByWatchId(watch.getId());
        String imageUrl = watchImages.isEmpty() ? null : watchImages.get(0).getImageUrl();

        WatchDTO watchDTO = new WatchDTO();
        watchDTO.setWatchImages(imageUrl != null ? List.of(imageUrl) : null);
        watchDTO.setId(watch.getId());
        watchDTO.setName(watch.getName());
        watchDTO.setStatus(watch.getStatus());
        watchDTO.setDescription(watch.getDescription());
        watchDTO.setPrice(watch.getPrice());
        watchDTO.setBrandName(watch.getBrand().getBrandName());
        watchDTO.setYearProduced(watch.getYearProduced());
        watchDTO.setModel(watch.getModel());
        watchDTO.setMaterial(watch.getMaterial());
        watchDTO.setWatchStrap(watch.getWatchStrap());
        watchDTO.setSize(watch.getSize());
        watchDTO.setAccessories(watch.getAccessories());
        watchDTO.setReferenceCode(watch.getReferenceCode());
        watchDTO.setWatchTypeName(watch.getWatchType().getTypeName());
        watchDTO.setUserId(watch.getUser().getId());
        watchDTO.setUserName(watch.getUser().getName());
        watchDTO.setUserAvatar(watch.getUser().getAvatar());
        watchDTO.setUserPhoneNumber(watch.getUser().getPhoneNumber());
        watchDTO.setUserRatingScore(watch.getUser().getRatingScore());
        watchDTO.setAddress(watch.getAddress());
        watchDTO.setArea(watch.getArea());
        watchDTO.setCreateDate(watch.getCreateDate());
        watchDTO.setEndDate(watch.getEndDate());
        watchDTO.setStartDate(watch.getStartDate());
        watchDTO.setEndDate(watch.getEndDate());
        watchDTO.setNumberDatePost(watch.getNumberDatePost());
        watchDTO.setHasAppraisalCertificate(watch.isHasAppraisalCertificate());
        watchDTO.setAppraisalCertificateUrl(watch.getAppraisalCertificateUrl());

        return watchDTO;
    }

    private ShowWatchDTO convertToShowWatchDTO(Watch watch) {
        List<WatchImage> images = watchImageRepository.findByWatchId(watch.getId());
        String imageUrl = images.isEmpty() ? null : images.get(0).getImageUrl();
        return new ShowWatchDTO(
                watch.getId(),
                watch.getName(),
                watch.getPrice(),
                watch.getStatus(),
                imageUrl,
                watch.getUser().getId(),
                watch.getUser().getName(),
                watch.getUser().getAvatar(),
                watch.getUser().getRatingScore(),
                watch.getArea(),
                watch.getAddress(),
                watch.getCreateDate(),
                watch.isHasAppraisalCertificate(),
                watch.getAppraisalCertificateUrl()

        );
    }

    @Transactional
    @Override
    public Boolean createWatch(CreateWatchDTO watchDTO) {
        User user = userRepository.findById(watchDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        Brand brand = brandRepository.findById(watchDTO.getBrandId())
                .orElseThrow(() -> new RuntimeException("Brand not found"));
        WatchType watchType = watchTypeRepository.findById(watchDTO.getWatchTypeId())
                .orElseThrow(() -> new RuntimeException("Watch type not found"));

        Watch watch = new Watch();
        watch.setUser(user);
        watch.setName(watchDTO.getName());
        watch.setWatchStatus(watchDTO.getWatchStatus());
        watch.setDescription(watchDTO.getDescription());
        watch.setPrice(watchDTO.getPrice());
        watch.setBrand(brand);
        watch.setYearProduced(watchDTO.getYearProduced());
        watch.setModel(watchDTO.getModel());
        watch.setMaterial(watchDTO.getMaterial());
        watch.setWatchStrap(watchDTO.getWatchStrap());
        watch.setSize(watchDTO.getSize());
        watch.setAccessories(watchDTO.getAccessories());
        watch.setReferenceCode(watchDTO.getReferenceCode());
        watch.setPlaceOfProduction(watchDTO.getPlaceOfProduction());
        watch.setWatchType(watchType);
        watch.setStatus("SHOW");
        watch.setAddress(watchDTO.getAddress());
        watch.setCreateDate(new Date());
        watch.setUpdateDate(new Date());
        watch.setArea(watchDTO.getArea());
        watch.setExpired(true);

        watch.setHasAppraisalCertificate(watchDTO.isHasAppraisalCertificate());
        if (watchDTO.isHasAppraisalCertificate()) {
            MultipartFile appraisalCertificateFile = watchDTO.getAppraisalCertificateFile();
            if (appraisalCertificateFile != null && !appraisalCertificateFile.isEmpty()) {
                try {
                    Map uploadResult = cloudinaryService.uploadFile(appraisalCertificateFile);
                    String appraisalCertificateUrl = uploadResult.get("url").toString();
                    watch.setAppraisalCertificateUrl(appraisalCertificateUrl);
                } catch (Exception e) {
                    throw new RuntimeException("Failed to upload appraisal certificate", e);
                }
            }
        }

        Watch savedWatch = watchRepository.save(watch);

        for (MultipartFile imageFile : watchDTO.getImageFiles()) {
            try {
                Map uploadResult = cloudinaryService.uploadFile(imageFile);
                String uploadedImageUrl = uploadResult.get("url").toString();
                WatchImage requestImage = new WatchImage();
                requestImage.setWatch(savedWatch);
                requestImage.setImageUrl(uploadedImageUrl);
                watchImageRepository.save(requestImage);
            } catch (Exception e) {
                throw new RuntimeException("Failed to upload image", e);
            }
        }
        return true;

    }

    @Override
    @Transactional
    public Watch updateStatus(Long id, String newStatus) {
        Watch watch = watchRepository.findById(id).orElseThrow(() -> new RuntimeException("Watch not found"));
        watch.setStatus(newStatus);
        return watchRepository.save(watch);
    }

    @Transactional
    @Override
    public Watch updateWatch(Long watchId, WatchUpdateRequestDTO updateRequest) {
        Optional<Watch> optionalWatch = watchRepository.findById(watchId);
        if (optionalWatch.isPresent()) {
            Watch watch = optionalWatch.get();

            // Update basic fields from DTO
            watch.setName(updateRequest.getName());
            watch.setWatchStatus(updateRequest.getWatchStatus());
            watch.setDescription(updateRequest.getDescription());
            watch.setPrice(updateRequest.getPrice());
            watch.setYearProduced(updateRequest.getYearProduced());
            watch.setModel(updateRequest.getModel());
            watch.setMaterial(updateRequest.getMaterial());
            watch.setWatchStrap(updateRequest.getWatchStrap());
            watch.setSize(updateRequest.getSize());
            watch.setAccessories(updateRequest.getAccessories());
            watch.setReferenceCode(updateRequest.getReferenceCode());
            watch.setPlaceOfProduction(updateRequest.getPlaceOfProduction());
            watch.setAddress(updateRequest.getAddress());
            watch.setUpdateDate(new Date());
            watch.setArea(updateRequest.getArea());

            // Update Brand if provided
            if (updateRequest.getBrandId() != null) {
                Optional<Brand> optionalBrand = brandRepository.findById(updateRequest.getBrandId());
                optionalBrand.ifPresent(watch::setBrand);
            }

            if (updateRequest.getImageFiles() != null && !updateRequest.getImageFiles().isEmpty()) {
                // Delete current images associated with the watch
                watchImageRepository.deleteByWatch(watch);

                // Save new images
                List<WatchImage> newImages = new ArrayList<>();
                for (MultipartFile imageFile : updateRequest.getImageFiles()) {
                    try {
                        Map<String, Object> uploadResult = cloudinaryService.uploadFile(imageFile);
                        String uploadedImageUrl = uploadResult.get("url").toString();
                        WatchImage newImage = new WatchImage();
                        newImage.setWatch(watch);
                        newImage.setImageUrl(uploadedImageUrl);
                        newImages.add(newImage);
                    } catch (Exception e) {
                        throw new RuntimeException("Failed to upload image", e);
                    }
                }

                // Save all new images
                watchImageRepository.saveAll(newImages);
            }
            // Save the updated watch entity
            return watchRepository.save(watch);
        } else {
            // Handle watch not found case
            throw new RuntimeException("Watch not found with id: " + watchId);
        }
    }

    @Override
    public WatchDTO getWatchById(Long id) {
        Optional<Watch> watchOptional = watchRepository.findById(id);
        if (watchOptional.isPresent()) {
            Watch watch = watchOptional.get();
            WatchDTO watchDTO = convertToDTO(watch);

            User user = watch.getUser();
            if (user != null) {
                watchDTO.setUserId(user.getId());
                watchDTO.setUserName(user.getName());
                watchDTO.setUserAvatar(user.getAvatar());
                watchDTO.setUserPhoneNumber(user.getPhoneNumber());
                watchDTO.setUserRatingScore(user.getRatingScore());
            }

            List<WatchImage> watchImages = watchImageRepository.findByWatchId(watch.getId());
            List<String> imageUrls = watchImages.stream()
                    .map(WatchImage::getImageUrl)
                    .collect(Collectors.toList());
            watchDTO.setWatchImages(imageUrls);

            List<FeedbackDTO> feedbacks = feedbackService.getFeedbackByWatchId(watch.getId());
            watchDTO.setFeedbacks(feedbacks);
            return watchDTO;
        } else {
            throw new RuntimeException("Watch not found with id: " + id);
        }
    }

    private WatchDTO convertToDTO(Watch watch) {
        WatchDTO watchDTO = new WatchDTO();
        watchDTO.setId(watch.getId());
        watchDTO.setUserId(watch.getUser().getId());
        watchDTO.setName(watch.getName());
        watchDTO.setWatchStatus(watch.getWatchStatus());
        watchDTO.setStatus(watch.getStatus());
        watchDTO.setDescription(watch.getDescription());
        watchDTO.setPrice(watch.getPrice());
        watchDTO.setBrandName(watch.getBrand().getBrandName());
        watchDTO.setYearProduced(watch.getYearProduced());
        watchDTO.setModel(watch.getModel());
        watchDTO.setMaterial(watch.getMaterial());
        watchDTO.setWatchStrap(watch.getWatchStrap());
        watchDTO.setSize(watch.getSize());
        watchDTO.setAccessories(watch.getAccessories());
        watchDTO.setReferenceCode(watch.getReferenceCode());
        watchDTO.setPlaceOfProduction(watch.getPlaceOfProduction());
        watchDTO.setAddress(watch.getAddress());
        watchDTO.setArea(watch.getArea());
        watchDTO.setCreateDate(watch.getCreateDate());
        watchDTO.setUpdateDate(watch.getUpdateDate());
        watchDTO.setWatchTypeName(watch.getWatchType().getTypeName());
        watchDTO.setHasAppraisalCertificate(watch.isHasAppraisalCertificate());
        watchDTO.setAppraisalCertificateUrl(watch.getAppraisalCertificateUrl());
        return watchDTO;
    }


    @Override
    public List<WatchSellerDTO> getWatchesByUserIdAndStatus(Long userId, String status) {
        List<Watch> watches;
        if (status == null) {
            watches = watchRepository.findByUserId(userId);
        } else {
            watches = watchRepository.findByUserIdAndStatus(userId, status);
        }
        return watches.stream()
                .map(this::convertToSellerDTO)
                .collect(Collectors.toList());
    }

    private WatchSellerDTO convertToSellerDTO(Watch watch) {
        String imageUrl = watch.getImages().stream()
                .map(WatchImage::getImageUrl)
                .findFirst()
                .orElse(null);

        String renewalPackageName = watch.getRenewalPackage() != null
                ? watch.getRenewalPackage().getName()
                :"Đăng tin bình thường";

        WatchSellerDTO watchDTO = new WatchSellerDTO();
        watchDTO.setId(watch.getId());
        watchDTO.setImageUrl(imageUrl);
        watchDTO.setName(watch.getName());
        watchDTO.setSize(watch.getSize());
        watchDTO.setType(watch.getWatchType().getTypeName());
        watchDTO.setBrand(watch.getBrand().getBrandName());
        watchDTO.setPrice(watch.getPrice());
        watchDTO.setCreateDate(watch.getCreateDate());
        watchDTO.setAddress(watch.getAddress());
        watchDTO.setArea(watch.getArea());
        watchDTO.setStatus(watch.getStatus());
        watchDTO.setStartDate(watch.getStartDate());
        watchDTO.setEndDate(watch.getEndDate());
        watchDTO.setNumberDatePost(watch.getNumberDatePost());
        watchDTO.setTypePost(renewalPackageName);
        watchDTO.setHasAppraisalCertificate(watch.isHasAppraisalCertificate());
        watchDTO.setAppraisalCertificateUrl(watch.getAppraisalCertificateUrl());


        return watchDTO;
    }

    @Override
    public List<WatchSellerDTO> getWatchesByUserId(Long userId) {
        List<Watch> watches = watchRepository.findByUserId(userId);
        return watches.stream()
                .map(this::convertToSellerDTO)
                .collect(Collectors.toList());
    }



    @Override
    public Page<SearchWatchDTO> searchWatchesByKeywordAndFilter(String keyword, Double minPrice, Double maxPrice, List<String> areaList,
                                                                List<String> typeList, List<String> brandList, List<String> watchStatusList,
                                                                String status, List<String> accessoriesList, String name, Pageable pageable) {
        Page<Watch> watches = watchRepository.searchByKeywordAndFilter(keyword, minPrice, maxPrice, areaList, typeList, brandList, watchStatusList, status, accessoriesList, name, pageable);
        Page<SearchWatchDTO> searchWatchDTOs = new PageImpl<>(
                watches.stream().map(this::convertToSearchWatchDTO).collect(Collectors.toList()), pageable, watches.getTotalElements());
        return searchWatchDTOs;
    }

    private SearchWatchDTO convertToSearchWatchDTO(Watch watch) {
        SearchWatchDTO searchWatchDTO = new SearchWatchDTO();
        searchWatchDTO.setId(watch.getId());
        searchWatchDTO.setName(watch.getName());
        searchWatchDTO.setPrice(watch.getPrice());
        searchWatchDTO.setWatchstatus(watch.getWatchStatus());
        searchWatchDTO.setBrand(watch.getBrand().getBrandName());
        searchWatchDTO.setType(watch.getWatchType().getTypeName());
        searchWatchDTO.setStatus(watch.getStatus());
        searchWatchDTO.setAccessories(watch.getAccessories());
        searchWatchDTO.setArea(watch.getArea());
        searchWatchDTO.setAddress(watch.getAddress());
        searchWatchDTO.setSellerId(watch.getUser().getId());
        searchWatchDTO.setSellerName(watch.getUser().getName());
        searchWatchDTO.setSellerImage(watch.getUser().getAvatar());
        searchWatchDTO.setCreateDate(watch.getCreateDate());
        searchWatchDTO.setHasAppraisalCertificate(watch.isHasAppraisalCertificate());
        List<WatchImage> images = watch.getImages();
        if (images != null && !images.isEmpty()) {
            searchWatchDTO.setImageUrl(images.get(0).getImageUrl());
        }

        return searchWatchDTO;
    }



    @Override
    @Transactional
    public RenewalPackageDTO renewWatch(Long watchId, Long renewalPackageId) {
        Watch watch = watchRepository.findById(watchId)
                .orElseThrow(() -> new RuntimeException("Watch not found"));

        RenewalPackage renewalPackage = renewalPackageRepository.findById(renewalPackageId)
                .orElseThrow(() -> new RuntimeException("Renewal package not found"));

        Date now = new Date();
        Date newEndTime;

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);

        // Nếu tin đăng đã hết hạn hoặc chưa từng gia hạn
        if (watch.getEndDate() == null || watch.getEndDate().before(now)) {
            calendar.add(Calendar.DAY_OF_MONTH, renewalPackage.getDuration());
            newEndTime = calendar.getTime();
            watch.setNumberDatePost(renewalPackage.getDuration());
        } else {
            // Nếu tin đăng vẫn còn hạn, gia hạn thêm vào endTime hiện tại
            calendar.setTime(watch.getEndDate());
            calendar.add(Calendar.DAY_OF_MONTH, renewalPackage.getDuration());
            newEndTime = calendar.getTime();
            watch.setNumberDatePost(watch.getNumberDatePost() + renewalPackage.getDuration());
        }

        watch.setStartDate(now);
        watch.setEndDate(newEndTime);
        watch.setRenewalPackage(renewalPackage);
        watch.setExpired(false);

        watch = watchRepository.save(watch);


        RenewalPackageDTO renewalPackageDTO = convertToRenewalPackageDTO(watch);
        renewalPackageDTO.setTotalPrice(renewalPackage.getPrice());

        return renewalPackageDTO;
    }

    private RenewalPackageDTO convertToRenewalPackageDTO(Watch watch) {
        String imageUrl = watch.getImages().stream()
                .map(WatchImage::getImageUrl)
                .findFirst()
                .orElse(null);

        RenewalPackageDTO renewalPackageDTO = new RenewalPackageDTO();
        renewalPackageDTO.setImageUrl(imageUrl);
        renewalPackageDTO.setName(watch.getName());
        renewalPackageDTO.setSize(watch.getSize());
        renewalPackageDTO.setPrice(watch.getPrice());
        renewalPackageDTO.setCreateDate(watch.getCreateDate());
        renewalPackageDTO.setAddress(watch.getAddress());
        renewalPackageDTO.setArea(watch.getArea());
        renewalPackageDTO.setStatus(watch.getStatus());
        renewalPackageDTO.setStartDate(watch.getStartDate());
        renewalPackageDTO.setEndDate(watch.getEndDate());
        renewalPackageDTO.setNumberDatePost(watch.getNumberDatePost());
        renewalPackageDTO.setTypePost(watch.getRenewalPackage().getName());
        renewalPackageDTO.setHasAppraisalCertificate(watch.isHasAppraisalCertificate());

        return renewalPackageDTO;
    }

    @Override
    @Transactional
    @Scheduled(cron = "0 0 0 * * ?") // Chạy mỗi ngày lúc 00:00
    public void checkAndExpireWatches() {
        List<Watch> watches = watchRepository.findAll();
        Date now = new Date();

        for (Watch watch : watches) {
            if (watch.getEndDate() != null && watch.getEndDate().before(now)) {
                watch.setExpired(true);
                watchRepository.save(watch);
            }
        }
    }

//    private Date calculateVipEndDate(int vipDays) {
//        Calendar calendar = Calendar.getInstance();
//        calendar.add(Calendar.DAY_OF_YEAR, vipDays);
//        return calendar.getTime();
//    }


    public List<WatchDTO> getAllActiveWatchesSortedByStartDate() {
        List<Watch> watches = watchRepository.findAllByExpiredFalseOrderByStartDateAsc();
        return watches.stream()
                .map(this::convertToWatchDTO)
                .collect(Collectors.toList());
    }
}
