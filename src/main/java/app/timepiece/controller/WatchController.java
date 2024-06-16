package app.timepiece.controller;

import app.timepiece.dto.CreateWatchDTO;
import app.timepiece.dto.ShowWatchDTO;
import app.timepiece.dto.WatchDTO;
import app.timepiece.entity.Watch;
import app.timepiece.service.WatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@CrossOrigin
@RestController
@RequestMapping("/api/watches")
public class WatchController {

    @Autowired
    private WatchService watchService;

   // @PreAuthorize("hasRole('Admin')")
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

    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<String> createWatch(@ModelAttribute CreateWatchDTO watchDTO) {
        try {
            Boolean isCreated = watchService.createWatch(watchDTO);
            if (isCreated) {
                return ResponseEntity.ok("Watch created successfully");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create watch");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<String> updateStatus(@PathVariable Long id, @RequestParam String status) {
         watchService.updateStatus(id, status);
        return ResponseEntity.ok("Watch status updated successfully");
    }

}
