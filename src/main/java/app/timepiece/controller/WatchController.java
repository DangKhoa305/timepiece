package app.timepiece.controller;

import app.timepiece.dto.*;
import app.timepiece.entity.Watch;
import app.timepiece.service.WatchService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


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

//    @GetMapping("/search")
//    public List<ShowWatchDTO> searchWatchesByName(@RequestParam String name) {
//        return watchService.searchWatchesByName(name);
//    }

    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<String> createWatch(@Valid @ModelAttribute CreateWatchDTO watchDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errors = bindingResult.getFieldErrors().stream()
                    .map(error -> error.getField() + ": " + error.getDefaultMessage())
                    .collect(Collectors.joining("\n"));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Validation errors:\n" + errors);
        }
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
            @Valid @ModelAttribute WatchUpdateRequestDTO updateRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errors = bindingResult.getFieldErrors().stream()
                    .map(error -> error.getField() + ": " + error.getDefaultMessage())
                    .collect(Collectors.joining("\n"));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Validation errors:\n" + errors);
        }
       watchService.updateWatch(watchId, updateRequest);
        return ResponseEntity.ok("Watch updated successfully");
    }


    @GetMapping("/{id}/getWatchById")
    public ResponseEntity<WatchDTO> getWatchById(@PathVariable Long id) {
        WatchDTO watchDTO = watchService.getWatchById(id);
        return ResponseEntity.ok(watchDTO);
    }

    @GetMapping("/user/{userId}/status")
    public ResponseEntity<List<WatchSellerDTO>> getWatchesByUserIdAndStatus(
            @PathVariable Long userId,
            @RequestParam(required = false) String status) {
        List<WatchSellerDTO> watches = watchService.getWatchesByUserIdAndStatus(userId, status);
        return ResponseEntity.ok(watches);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<WatchSellerDTO>> getWatchesByUserId(@PathVariable Long userId) {
        List<WatchSellerDTO> watches = watchService.getWatchesByUserId(userId);
        return ResponseEntity.ok(watches);
    }

//    @GetMapping("/searchWatch")
//    public Page<SearchWatchDTO> searchWatches(
//            @RequestParam(required = false) Double price,
//            @RequestParam(required = false) String area,
//            @RequestParam(required = false) String type,
//            @RequestParam(required = false) String brand,
//            @RequestParam(required = false) String watchStatus,
//            @RequestParam(required = false) String status,
//            @RequestParam(required = false) String accessories,
//            @RequestParam(required = false) String name,
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "10") int size) {
//
//        Pageable pageable = PageRequest.of(page, size);
//        return watchService.searchWatches(price, area, type, brand, watchStatus, status, accessories, name , pageable);
//    }
//
//    @GetMapping("/searchWatchByKeyword")
//    public Page<SearchWatchDTO> searchWatchesByKeyword(
//            @RequestParam(required = false) String keyword,
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "10") int size) {
//
//        Pageable pageable = PageRequest.of(page, size);
//        return watchService.searchWatchesByKeyword(keyword, pageable);
//    }


    @GetMapping("/searchWatchByKeywordAndFilter")
    public Page<SearchWatchDTO> searchWatchesByKeywordAndFilter(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) String area,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) String watchStatus,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String accessories,
            @RequestParam(required = false) String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return watchService.searchWatchesByKeywordAndFilter(keyword, minPrice, maxPrice, area, type, brand, watchStatus,
                status, accessories, name, pageable);
    }

    @PostMapping("/{id}/renew")
    public ResponseEntity<?> renewWatch(@PathVariable Long watchId, @RequestParam Long renewalPackageId) {
        try {
        RenewalPackageDTO updatedWatchDTO = watchService.renewWatch(watchId,renewalPackageId);
        return ResponseEntity.ok(updatedWatchDTO);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

//    @PutMapping("/{watchid}/vip")
//    public ResponseEntity<?> makeWatchVip(
//            @PathVariable Long watchid,
//            @RequestParam int vipDays,
//            @RequestParam Long userId,
//            @RequestParam double vipFee) {
//        try {
//            WatchDTO updatedWatchDTO = watchService.makeWatchVip(watchid, vipDays, userId, vipFee);
//            return ResponseEntity.ok(updatedWatchDTO);
//        } catch (RuntimeException e) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
//        }
//    }

    @GetMapping("/getAllActiveWatchesSortedByStartDate")
    public ResponseEntity<List<WatchDTO>> getActiveWatches() {
        List<WatchDTO> watches = watchService.getAllActiveWatchesSortedByStartDate();
        return ResponseEntity.ok(watches);
    }
}
