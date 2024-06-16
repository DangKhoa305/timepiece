package app.timepiece.service;

import app.timepiece.dto.CreateWatchDTO;
import app.timepiece.dto.ShowWatchDTO;
import app.timepiece.dto.WatchDTO;
import app.timepiece.entity.Watch;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

public interface WatchService {
    List<WatchDTO> getAllWatches();
    List<ShowWatchDTO> getTop12Watches();
    List<ShowWatchDTO> searchWatchesByName(String name);
    Boolean createWatch(CreateWatchDTO watchDTO) throws IOException;
    Watch updateStatus(Long id, String newStatus);
}