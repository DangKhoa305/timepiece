package app.timepiece.service;

import app.timepiece.dto.CreateFeedbackDTO;
import app.timepiece.dto.FeedbackDTO;
import app.timepiece.entity.Feedback;

import java.util.List;

public interface FeedbackService {
    List<FeedbackDTO> getAllFeedbacks();
    FeedbackDTO getFeedbackById(Long id);
    Feedback updateFeedback(Long id, Feedback feedback);
    void deleteFeedback(Long id);
    FeedbackDTO createFeedback(CreateFeedbackDTO createFeedbackDTO);
    List<FeedbackDTO> getFeedbackByOrderId(Long orderId);
    List<FeedbackDTO> getFeedbackByWatchId(Long watchId);
    List<FeedbackDTO> getFeedbacksBySellerId(Long sellerId);
}
