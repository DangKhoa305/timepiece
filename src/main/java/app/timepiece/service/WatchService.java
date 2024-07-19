package app.timepiece.service;

import app.timepiece.dto.*;
import app.timepiece.entity.Watch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

public interface WatchService {
    List<WatchDTO> getAllWatches();
//    List<ShowWatchDTO> getTop12Watches();
    List<ShowWatchDTO> getTop12WatchesByStatus();
    List<ShowWatchDTO> searchWatchesByName(String name);
    Boolean createWatch(CreateWatchDTO watchDTO) throws IOException;
    Watch updateStatus(Long id, String newStatus);
    Watch updateWatch(Long id, WatchUpdateRequestDTO updateRequest);
    WatchDTO getWatchById(Long id);
    List<WatchSellerDTO> getWatchesByUserIdAndStatus(Long userId, String status);
    //Page<SearchWatchDTO> searchWatches(Double price, String area, String type, String brand, String watchStatus, String status, String accessories, String name, Pageable pageable);
    //Page<SearchWatchDTO> searchWatchesByKeyword(String keyword, Pageable pageable);
//WatchDTO makeWatchVip(Long id, int vipDays, Long userId, double vipFee);
    Page<SearchWatchDTO> searchWatchesByKeywordAndFilter(String keyword, Double minPrice, Double maxPrice, String area,
                                                         String type, String brand, String watchStatus, String status,
                                                         String accessories, String name, Pageable pageable);
    List<WatchSellerDTO> getWatchesByUserId(Long userId);

    @Transactional
    RenewalPackageDTO renewWatch(Long watchId, Long renewalPackageId);

    @Transactional
    @Scheduled(cron = "0 0 0 * * ?") // Chạy mỗi ngày lúc 00:00
    void checkAndExpireWatches();
}