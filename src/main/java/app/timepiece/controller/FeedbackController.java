package app.timepiece.controller;

import app.timepiece.dto.CreateFeedbackDTO;
import app.timepiece.dto.FeedbackDTO;
import app.timepiece.entity.Feedback;
import app.timepiece.service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/feedbacks")
public class FeedbackController {

    @Autowired
    private FeedbackService feedbackService;

    @PostMapping("CreateFeedback")
    public ResponseEntity<?> createFeedback(@RequestBody CreateFeedbackDTO createFeedbackDTO) {
        try {
            FeedbackDTO savedFeedback = feedbackService.createFeedback(createFeedbackDTO);
            return ResponseEntity.ok(savedFeedback);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllFeedbacks() {
        try {
            List<FeedbackDTO> savedFeedback = feedbackService.getAllFeedbacks();
            return ResponseEntity.ok(savedFeedback);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }


    @GetMapping("/{id}")
    public ResponseEntity<FeedbackDTO> getFeedbackById(@PathVariable Long id) {
        FeedbackDTO feedbackDTO = feedbackService.getFeedbackById(id);
        return ResponseEntity.ok(feedbackDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Feedback> updateFeedback(@PathVariable Long id, @RequestBody Feedback feedback) {
        return ResponseEntity.ok(feedbackService.updateFeedback(id, feedback));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFeedback(@PathVariable Long id) {
        feedbackService.deleteFeedback(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<FeedbackDTO>> getFeedbackByOrderId(@PathVariable Long orderId) {
        try {
            List<FeedbackDTO> feedbackList = feedbackService.getFeedbackByOrderId(orderId);
            return ResponseEntity.ok(feedbackList);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/watch/{watchId}")
    public ResponseEntity<List<FeedbackDTO>> getFeedbackByWatchId(@PathVariable Long watchId) {
        try {
            List<FeedbackDTO> feedbackList = feedbackService.getFeedbackByWatchId(watchId);
            return ResponseEntity.ok(feedbackList);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/seller/{sellerId}")
    public ResponseEntity<?> getFeedbacksBySellerId(@PathVariable Long sellerId) {
        try {
            List<FeedbackDTO> feedbacks = feedbackService.getFeedbacksBySellerId(sellerId);
            return ResponseEntity.ok(feedbacks);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

}
