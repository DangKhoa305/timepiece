package app.timepiece.controller;

import app.timepiece.dto.*;
import app.timepiece.entity.Watch;
import app.timepiece.service.WatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

//    @GetMapping("/top12")
//    public List<ShowWatchDTO> getTop12Watches() {
//        return watchService.getTop12Watches();
//    }

    @GetMapping("/top12/Approved")
    public List<ShowWatchDTO> getTop12WatchesApproved() {
        return watchService.getTop12WatchesByStatus();
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

    @PutMapping(value = "/{watchId}", consumes = {"multipart/form-data"})
    public ResponseEntity<String> updateWatch(
            @PathVariable Long watchId,
            @ModelAttribute WatchUpdateRequestDTO updateRequest) {
       watchService.updateWatch(watchId, updateRequest);
        return ResponseEntity.ok("Watch updated successfully");
    }


    @GetMapping("/{id}/getWatchById")
    public ResponseEntity<WatchDTO> getWatchById(@PathVariable Long id) {
        WatchDTO watchDTO = watchService.getWatchById(id);
        return ResponseEntity.ok(watchDTO);
    }

    @GetMapping("/user/{userId}/status/{status}")
    public ResponseEntity<List<WatchSellerDTO>> getWatchesByUserIdAndStatus(@PathVariable Long userId, @PathVariable String status) {
        List<WatchSellerDTO> watches = watchService.getWatchesByUserIdAndStatus(userId, status);
        return ResponseEntity.ok(watches);
    }

    @GetMapping("/searchWatch")
    public Page<SearchWatchDTO> searchWatches(
            @RequestParam(required = false) Double price,
            @RequestParam(required = false) String address,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) String watchStatus,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String accessories,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return watchService.searchWatches(price, address, type, brand, watchStatus, status, accessories, pageable);
    }
}
