package app.timepiece.service.serviceImpl;

import app.timepiece.dto.*;
import app.timepiece.entity.*;
import app.timepiece.repository.*;
import app.timepiece.service.WalletService;
import app.timepiece.service.WatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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


    @Override
    public List<WatchDTO> getAllWatches() {
        return watchRepository.findAll().stream().map(this::convertToWatchDTO).collect(Collectors.toList());
    }

//    @Override
//    public List<ShowWatchDTO> getTop12Watches() {
//        List<Watch> watches = watchRepository.findAll().stream().limit(12).collect(Collectors.toList());
//        return watches.stream().map(this::convertToShowWatchDTO).collect(Collectors.toList());
//    }

    public List<ShowWatchDTO> getTop12WatchesByStatus() {
        List<Watch> watches = watchRepository.findByStatusOrderByCreateDateDesc("Approved").stream().limit(12).collect(Collectors.toList());
        return watches.stream().map(this::convertToShowWatchDTO).collect(Collectors.toList());
    }

    @Override
    public List<ShowWatchDTO> searchWatchesByName(String name) {
        List<Watch> watches = watchRepository.findByNameContainingIgnoreCase(name);
        return watches.stream().map(this::convertToShowWatchDTO).collect(Collectors.toList());
    }

    private WatchDTO convertToWatchDTO(Watch watch) {
        WatchDTO watchDTO = new WatchDTO();
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
                imageUrl
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
        watch.setStatus("Approved");
        watch.setAddress(watchDTO.getAddress());
        watch.setCreateDate(new Date());
        watch.setUpdateDate(new Date());

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
            watch.setStatus("wait");
            watch.setUpdateDate(new Date());

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
            return convertToDTO(watch);
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
        watchDTO.setCreateDate(watch.getCreateDate());
        watchDTO.setUpdateDate(watch.getUpdateDate());
        watchDTO.setWatchTypeName(watch.getWatchType().getTypeName());
        return watchDTO;
    }


    @Override
    public List<WatchSellerDTO> getWatchesByUserIdAndStatus(Long userId, String status) {
        List<Watch> watches = watchRepository.findByUserIdAndStatus(userId, status);
        return watches.stream()
                .map(this::convertToSellerDTO)
                .collect(Collectors.toList());
    }

    private WatchSellerDTO convertToSellerDTO(Watch watch) {
        WatchSellerDTO watchDTO = new WatchSellerDTO();
        watchDTO.getImageUrl();
        watchDTO.setName(watch.getName());
        watchDTO.setSize(watch.getSize());
        watchDTO.setPrice(watch.getPrice());
        watchDTO.setCreateDate(watch.getCreateDate());
        watchDTO.setAddress(watch.getAddress());
        return watchDTO;
    }

    @Override
    public Page<SearchWatchDTO> searchWatches(Double price, String address, String type, String brand, String watchStatus, String status, String accessories, String name, Pageable pageable) {
        Page<Watch> watches = watchRepository.searchWatches(price, address, type, brand, watchStatus, status, accessories, name, pageable);
        Page<SearchWatchDTO> searchwatchDTOs = new PageImpl<>(
                watches.stream().map(this::convertToSearchWatchDTO).collect(Collectors.toList()), pageable, watches.getTotalElements());
        return searchwatchDTOs;
    }

    @Override
    public Page<SearchWatchDTO> searchWatchesByKeyword(String keyword, Pageable pageable) {
        Page<Watch> watches = watchRepository.searchWatchesByKeyword(keyword, pageable);
        Page<SearchWatchDTO> searchwatchDTOs = new PageImpl<>(
                watches.stream().map(this::convertToSearchWatchDTO).collect(Collectors.toList()), pageable, watches.getTotalElements());
        return searchwatchDTOs;
    }

    private SearchWatchDTO convertToSearchWatchDTO(Watch watch) {
        SearchWatchDTO searchWatchDTO = new SearchWatchDTO();
        searchWatchDTO.setId(watch.getId());
        searchWatchDTO.setName(watch.getName());
        searchWatchDTO.setPrice(watch.getPrice());
        searchWatchDTO.setStatus(watch.getWatchStatus());
        searchWatchDTO.setStatus(watch.getStatus());
        searchWatchDTO.setAccessories(watch.getAccessories());
        List<WatchImage> images = watch.getImages();
        if (images != null && !images.isEmpty()) {
            searchWatchDTO.setImageUrl(images.get(0).getImageUrl());
        }

        return searchWatchDTO;
    }

    @Transactional
    public WatchDTO makeWatchVip(Long id, int vipDays, Long userId, double vipFee) {
        Watch watch = watchRepository.findById(id).orElseThrow(() -> new RuntimeException("Watch not found"));

        // Lấy thông tin người dùng từ UserService
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        // Lấy wallet của người dùng từ thông tin user
        Wallet wallet = user.getWallet();

        // Kiểm tra và trừ tiền từ ví
        boolean withdrawSuccess = walletService.withdrawFromWallet(wallet.getId(), vipFee);
        if (!withdrawSuccess) {
            throw new RuntimeException("Withdrawal from wallet failed");
        }

        watch.setStatus("VIP");
        watch.setVipEndDate(calculateVipEndDate(vipDays));
        Watch savedWatch = watchRepository.save(watch);

        // Tạo đối tượng WatchDTO từ savedWatch
        WatchDTO watchDTO = WatchDTO.builder()
                .id(savedWatch.getId())
                .userId(user.getId())
                .name(savedWatch.getName())
                .watchStatus(savedWatch.getWatchStatus())
                .status(savedWatch.getStatus())
                .description(savedWatch.getDescription())
                .price(savedWatch.getPrice())
                .brandName(savedWatch.getBrand().getBrandName()) // Thay vì savedWatch.getBrandName() nếu Brand là một entity
                .yearProduced(savedWatch.getYearProduced())
                .model(savedWatch.getModel())
                .material(savedWatch.getMaterial())
                .watchStrap(savedWatch.getWatchStrap())
                .size(savedWatch.getSize())
                .accessories(savedWatch.getAccessories())
                .referenceCode(savedWatch.getReferenceCode())
                .placeOfProduction(savedWatch.getPlaceOfProduction())
                .address(savedWatch.getAddress())
                .createDate(savedWatch.getCreateDate())
                .updateDate(savedWatch.getUpdateDate())
                .watchTypeName(savedWatch.getWatchType().getTypeName()) // Thay vì savedWatch.getWatchTypeName() nếu WatchType là một entity
                .build();

        return watchDTO;
    }


    private Date calculateVipEndDate(int vipDays) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, vipDays);
        return calendar.getTime();
    }

}
