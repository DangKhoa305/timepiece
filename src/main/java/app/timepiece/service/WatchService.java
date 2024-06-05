package app.timepiece.service;

import app.timepiece.dto.ShowWatchDTO;
import app.timepiece.dto.WatchDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface WatchService {
    List<WatchDTO> getAllWatches();
    List<ShowWatchDTO> getTop12Watches();
    List<ShowWatchDTO> searchWatchesByName(String name);
}