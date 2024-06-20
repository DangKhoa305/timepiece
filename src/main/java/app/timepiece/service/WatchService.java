package app.timepiece.service;

import app.timepiece.dto.*;
import app.timepiece.entity.Watch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.util.List;

public interface WatchService {
    List<WatchDTO> getAllWatches();
    List<ShowWatchDTO> getTop12Watches();
    List<ShowWatchDTO> searchWatchesByName(String name);
    Boolean createWatch(CreateWatchDTO watchDTO) throws IOException;
    Watch updateStatus(Long id, String newStatus);
    Watch updateWatch(Long id, WatchUpdateRequestDTO updateRequest);
    WatchDTO getWatchById(Long id);
    List<WatchSellerDTO> getWatchesByUserIdAndStatus(Long userId, String status);
    Page<SearchWatchDTO> searchWatches(Double price, String address, String type, String brand, String watchStatus,String status, Pageable pageable);
}