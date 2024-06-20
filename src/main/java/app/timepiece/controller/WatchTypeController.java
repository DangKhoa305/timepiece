package app.timepiece.controller;

import app.timepiece.dto.WatchTypeDTO;
import app.timepiece.entity.WatchType;
import app.timepiece.service.WatchTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/watch-types")
public class WatchTypeController {

    @Autowired
    private WatchTypeService watchTypeService;

    @GetMapping("getAll")
    public List<WatchTypeDTO> getAllWatchTypes() {
        return watchTypeService.getAllWatchTypes();
    }

    @PostMapping("/createWatchType")
    public ResponseEntity<?> createWatchType(@RequestParam String typeName) {
        Optional<WatchType> watchType = watchTypeService.createWatchType(typeName);
        if (watchType.isPresent()) {
            return new ResponseEntity<>(watchType.get(), HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("Watch type already exists", HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}/updateWatchType")
    public WatchTypeDTO updateWatchTypeName(@PathVariable Long id, @RequestBody String typeName) {
        return watchTypeService.updateWatchTypeName(id, typeName);
    }
}