package app.timepiece.controller;


import app.timepiece.entity.Watch;
import app.timepiece.service.WatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/watches")
public class WatchController {

    @Autowired
    private WatchService watchService;

    @GetMapping("/getWatch")
    public List<Watch> getAllWatches() {
        return watchService.getAllWatches();
    }
}