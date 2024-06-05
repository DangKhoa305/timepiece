package app.timepiece.service;

import app.timepiece.dto.WatchDTO;
import app.timepiece.entity.Watch;
import app.timepiece.repository.WatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class WatchService {

    @Autowired
    private WatchRepository watchRepository;

    public List<WatchDTO> getAllWatches() {
        return watchRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    private WatchDTO convertToDTO(Watch watch) {
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
}