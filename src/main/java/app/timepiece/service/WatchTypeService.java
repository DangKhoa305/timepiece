package app.timepiece.service;

import app.timepiece.dto.WatchTypeDTO;
import app.timepiece.entity.WatchType;

import java.util.List;
import java.util.Optional;

public interface WatchTypeService {
    Optional<WatchType> createWatchType(String typeName);
    List<WatchTypeDTO> getAllWatchTypes();
    WatchTypeDTO updateWatchTypeName(Long id, String typeName);
}