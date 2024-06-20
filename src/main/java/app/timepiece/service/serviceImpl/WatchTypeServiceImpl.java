package app.timepiece.service.serviceImpl;

import app.timepiece.dto.WatchTypeDTO;
import app.timepiece.entity.WatchType;
import app.timepiece.repository.WatchTypeRepository;
import app.timepiece.service.WatchTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class WatchTypeServiceImpl implements WatchTypeService {

    @Autowired
    private WatchTypeRepository watchTypeRepository;

    @Override
    public List<WatchTypeDTO> getAllWatchTypes() {
        List<WatchType> watchTypes = watchTypeRepository.findAll();
        return watchTypes.stream()
                .map(watchType -> new WatchTypeDTO(watchType.getId(), watchType.getTypeName()))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<WatchType> createWatchType(String typeName) {
        if (watchTypeRepository.findByTypeName(typeName).isPresent()) {
            return Optional.empty();
        }
        WatchType watchType = new WatchType();
        watchType.setTypeName(typeName);
        return Optional.of(watchTypeRepository.save(watchType));
    }

    @Override
    public WatchTypeDTO updateWatchTypeName(Long id, String typeName) {
        Optional<WatchType> watchTypeOptional = watchTypeRepository.findById(id);

        if (watchTypeOptional.isPresent()) {
            WatchType watchType = watchTypeOptional.get();
            watchType.setTypeName(typeName);
            WatchType updatedWatchType = watchTypeRepository.save(watchType);
            return new WatchTypeDTO(updatedWatchType.getId(), updatedWatchType.getTypeName());
        } else {
            throw new RuntimeException("WatchType not found");
        }
    }
}