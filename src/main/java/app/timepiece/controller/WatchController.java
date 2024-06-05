package app.timepiece.controller;

import app.timepiece.dto.ShowWatchDTO;
import app.timepiece.dto.WatchDTO;
import app.timepiece.service.WatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/watches")
public class WatchController {

    @Autowired
    private WatchService watchService;

    @GetMapping("/getAll")
    public List<WatchDTO> getAllWatches() {
        return watchService.getAllWatches();
    }

    @GetMapping("/top12")
    public List<ShowWatchDTO> getTop12Watches() {
        return watchService.getTop12Watches();
    }

    @GetMapping("/search")
    public List<ShowWatchDTO> searchWatchesByName(@RequestParam String name) {
        return watchService.searchWatchesByName(name);
    }
}
