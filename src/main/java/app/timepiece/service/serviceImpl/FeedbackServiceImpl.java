package app.timepiece.service.serviceImpl;

import app.timepiece.dto.CreateFeedbackDTO;
import app.timepiece.dto.FeedbackDTO;
import app.timepiece.entity.Feedback;
import app.timepiece.entity.Order;
import app.timepiece.repository.FeedbackRepository;
import app.timepiece.repository.OrderRepository;
import app.timepiece.service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class FeedbackServiceImpl implements FeedbackService {

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Override
    public List<Feedback> getAllFeedbacks() {
        return feedbackRepository.findAll();
    }

    @Override
    public FeedbackDTO getFeedbackById(Long id) {
        Optional<Feedback> optionalFeedback = feedbackRepository.findById(id);
        if (optionalFeedback.isPresent()) {
            Feedback feedback = optionalFeedback.get();
            return FeedbackDTO.builder()
                    .id(feedback.getId())
                    .comment(feedback.getComment())
                    .timestamp(feedback.getTimestamp())
                    .userName(feedback.getUser() != null ? feedback.getUser().getName() : null)
                    .orderId(feedback.getOrder() != null ? feedback.getOrder().getId() : null)
                    .parentFeedbackId(feedback.getParentFeedback() != null ? feedback.getParentFeedback().getId() : null)
                    .build();
        }
        return null;
    }

    @Override
    public Feedback updateFeedback(Long id, Feedback feedback) {
        Feedback existingFeedback = feedbackRepository.findById(id).orElse(null);
        if (existingFeedback != null) {
            existingFeedback.setComment(feedback.getComment());
            existingFeedback.setTimestamp(feedback.getTimestamp());
            existingFeedback.setOrder(feedback.getOrder());
            existingFeedback.setParentFeedback(feedback.getParentFeedback());
            existingFeedback.setUser(feedback.getUser());
            return feedbackRepository.save(existingFeedback);
        }
        return null;
    }

    @Override
    public void deleteFeedback(Long id) {
        feedbackRepository.deleteById(id);
    }

    @Override
    public FeedbackDTO createFeedback(CreateFeedbackDTO createFeedbackDTO) {
        Feedback feedback = new Feedback();
        feedback.setComment(createFeedbackDTO.getComment());
        feedback.setTimestamp(Instant.now().toString());

        Optional<Order> orderOptional = orderRepository.findById(createFeedbackDTO.getOrderId());
        if (orderOptional.isPresent()) {
            feedback.setOrder(orderOptional.get());
        } else {
            throw new RuntimeException("Order not found");
        }

        if (createFeedbackDTO.getParentFeedbackId() != null) {
            Optional<Feedback> parentFeedbackOptional = feedbackRepository.findById(createFeedbackDTO.getParentFeedbackId());
            if (parentFeedbackOptional.isPresent()) {
                feedback.setParentFeedback(parentFeedbackOptional.get());
            } else {
                throw new RuntimeException("Parent feedback not found");
            }
        }

        Feedback savedFeedback = feedbackRepository.save(feedback);

        return FeedbackDTO.builder()
                .id(savedFeedback.getId())
                .comment(savedFeedback.getComment())
                .timestamp(savedFeedback.getTimestamp())
                .orderId(savedFeedback.getOrder().getId())
                .parentFeedbackId(savedFeedback.getParentFeedback() != null ? savedFeedback.getParentFeedback().getId() : null)
                .build();
    }

    @Override
    public List<FeedbackDTO> getFeedbackByOrderId(Long orderId) {
        List<Feedback> feedbackList = feedbackRepository.findByOrderId(orderId);
        Map<Long, FeedbackDTO> feedbackMap = new HashMap<>();

        feedbackList.forEach(feedback -> {
            FeedbackDTO feedbackDTO = FeedbackDTO.builder()
                    .id(feedback.getId())
                    .comment(feedback.getComment())
                    .timestamp(feedback.getTimestamp())
                    .orderId(feedback.getOrder().getId())
                    .parentFeedbackId(feedback.getParentFeedback() != null ? feedback.getParentFeedback().getId() : null)
                    .build();
            feedbackMap.put(feedback.getId(), feedbackDTO);
        });

        feedbackMap.values().forEach(feedbackDTO -> {
            if (feedbackDTO.getParentFeedbackId() != null) {
                FeedbackDTO parentDTO = feedbackMap.get(feedbackDTO.getParentFeedbackId());
                if (parentDTO != null) {
                    if (parentDTO.getChildFeedbacks() == null) {
                        parentDTO.setChildFeedbacks(new ArrayList<>());
                    }
                    parentDTO.getChildFeedbacks().add(feedbackDTO);
                }
            }
        });

        return feedbackMap.values().stream()
                .filter(feedbackDTO -> feedbackDTO.getParentFeedbackId() == null)
                .collect(Collectors.toList());
    }

    @Override
    public List<FeedbackDTO> getFeedbackByWatchId(Long watchId) {
        List<Feedback> feedbackList = feedbackRepository.findFeedbacksByWatchId(watchId);
        Map<Long, FeedbackDTO> feedbackMap = new HashMap<>();

        feedbackList.forEach(feedback -> {
            FeedbackDTO feedbackDTO = FeedbackDTO.builder()
                    .id(feedback.getId())
                    .comment(feedback.getComment())
                    .timestamp(feedback.getTimestamp())
                    .orderId(feedback.getOrder().getId())
                    .parentFeedbackId(feedback.getParentFeedback() != null ? feedback.getParentFeedback().getId() : null)
                    .build();
            feedbackMap.put(feedback.getId(), feedbackDTO);
        });

        feedbackMap.values().forEach(feedbackDTO -> {
            if (feedbackDTO.getParentFeedbackId() != null) {
                FeedbackDTO parentDTO = feedbackMap.get(feedbackDTO.getParentFeedbackId());
                if (parentDTO != null) {
                    if (parentDTO.getChildFeedbacks() == null) {
                        parentDTO.setChildFeedbacks(new ArrayList<>());
                    }
                    parentDTO.getChildFeedbacks().add(feedbackDTO);
                }
            }
        });

        return feedbackMap.values().stream()
                .filter(feedbackDTO -> feedbackDTO.getParentFeedbackId() == null)
                .collect(Collectors.toList());
    }
}
