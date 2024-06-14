package app.timepiece.service.serviceImpl;

import app.timepiece.dto.CreateWatchDTO;
import app.timepiece.dto.ShowWatchDTO;
import app.timepiece.dto.WatchDTO;
import app.timepiece.entity.*;
import app.timepiece.repository.*;
import app.timepiece.service.WatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
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


    @Override
    public List<WatchDTO> getAllWatches() {
        return watchRepository.findAll().stream().map(this::convertToWatchDTO).collect(Collectors.toList());
    }

    @Override
    public List<ShowWatchDTO> getTop12Watches() {
        List<Watch> watches = watchRepository.findAll().stream().limit(12).collect(Collectors.toList());
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
        watch.setStatus("wait");
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

}
