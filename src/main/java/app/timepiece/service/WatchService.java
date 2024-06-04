package app.timepiece.service;

import app.timepiece.entity.Watch;
import app.timepiece.repository.WatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WatchService {

    @Autowired
    private WatchRepository watchRepository;

    public List<Watch> getAllWatches() {
        return watchRepository.findAll();
    }
}
